package com.company.netflow;

import com.fakecompany.flow.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PushbackInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NetflowV5ToFlowReader implements Iterator< Flow >
{
    private final static Logger LOGGER = LoggerFactory.getLogger( NetflowV5ToFlowReader.class );

    protected PushbackInputStream input;
    private int packetCount = 0;
    private long totalCount = 0L;
    private long lastTimestamp = 0L;
    private NetflowV5Header header = new NetflowV5Header();
    private NetflowV5Packet packet = new NetflowV5Packet();
    private Flow flow = new Flow( 0L, 3232235777L, 0, 0L, 0, 0, 0, 0L, 0, 0, 0, 0, 0, 0L, 0L, 0);

    public Flow next( Flow flow ) throws IOException
    {
        if ( flow == null )
        {
            flow = this.flow;
        }

        while (input != null)
        {
            try
            {
                if ( packetCount == 0 )
                {
                    header.read( input );
                    packetCount = header.get( "count" );
                    lastTimestamp = header.get( "unixSecs" );
                }
                if ( packetCount > 0 )
                {
                    packet.read( input );
                    packet.toAvroFlow( flow );
                    // flow timestamp is populated from last seen netflow header "unixSecs" timestamp
                    flow.setTimestamp( lastTimestamp );
                    packetCount -= 1;
                    totalCount += 1;
                    return flow;
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();

                try
                {
                    input.close();
                }
                catch( Exception closeError )
                {
                    LOGGER.warn( closeError.toString() );
                }

                packetCount = 0;
                input = null;
                throw e;
            }
        }

        LOGGER.info( "failed after packet# " + totalCount );
        throw new NoSuchElementException();
    }

    public void sync( long l ) throws IOException
    {
        while ( totalCount < l && hasNext() )
        {
            next();
        }
    }

    public boolean pastSync(long l) throws IOException
    {
        return false;
    }

    public long tell() throws IOException
    {
        return totalCount;
    }

    public void close() throws IOException
    {
        if ( input == null )
        {
            return;
        }
        try
        {
            input.close();
        }
        catch( Exception e)
        {
            LOGGER.warn( e.toString() );
        }
        input = null;
    }

    public Iterator<Flow> iterator()
    {
        return this;
    }

    public boolean hasNext()
    {
        if ( input == null )
        {
            return false;
        }
        try
        {
            int next = input.read();

            if (next > -1)
            {
                input.unread( next );
                return true;
            }
        }
        catch ( Exception e )
        {
            LOGGER.warn( e.toString() );
        }
        input = null;
        return false;
    }

    public Flow next()
    {
        try
        {
            return next( flow );
        }
        catch( Exception e )
        {
            LOGGER.warn( e.toString() );
        }
        throw new NoSuchElementException();
    }

}
