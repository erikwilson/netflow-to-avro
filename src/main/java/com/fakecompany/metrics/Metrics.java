/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.fakecompany.metrics;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Metrics extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 1556704490684194034L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Metrics\",\"namespace\":\"com.fakecompany.metrics\",\"fields\":[{\"name\":\"addr\",\"type\":\"long\"},{\"name\":\"octets\",\"type\":\"long\"},{\"name\":\"packets\",\"type\":\"long\"},{\"name\":\"tcpFlags\",\"type\":{\"type\":\"map\",\"values\":\"long\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Metrics> ENCODER =
      new BinaryMessageEncoder<Metrics>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Metrics> DECODER =
      new BinaryMessageDecoder<Metrics>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<Metrics> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<Metrics> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Metrics>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this Metrics to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a Metrics from a ByteBuffer. */
  public static Metrics fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public long addr;
  @Deprecated public long octets;
  @Deprecated public long packets;
  @Deprecated public java.util.Map<java.lang.CharSequence,java.lang.Long> tcpFlags;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Metrics() {}

  /**
   * All-args constructor.
   * @param addr The new value for addr
   * @param octets The new value for octets
   * @param packets The new value for packets
   * @param tcpFlags The new value for tcpFlags
   */
  public Metrics(java.lang.Long addr, java.lang.Long octets, java.lang.Long packets, java.util.Map<java.lang.CharSequence,java.lang.Long> tcpFlags) {
    this.addr = addr;
    this.octets = octets;
    this.packets = packets;
    this.tcpFlags = tcpFlags;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return addr;
    case 1: return octets;
    case 2: return packets;
    case 3: return tcpFlags;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: addr = (java.lang.Long)value$; break;
    case 1: octets = (java.lang.Long)value$; break;
    case 2: packets = (java.lang.Long)value$; break;
    case 3: tcpFlags = (java.util.Map<java.lang.CharSequence,java.lang.Long>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'addr' field.
   * @return The value of the 'addr' field.
   */
  public java.lang.Long getAddr() {
    return addr;
  }

  /**
   * Sets the value of the 'addr' field.
   * @param value the value to set.
   */
  public void setAddr(java.lang.Long value) {
    this.addr = value;
  }

  /**
   * Gets the value of the 'octets' field.
   * @return The value of the 'octets' field.
   */
  public java.lang.Long getOctets() {
    return octets;
  }

  /**
   * Sets the value of the 'octets' field.
   * @param value the value to set.
   */
  public void setOctets(java.lang.Long value) {
    this.octets = value;
  }

  /**
   * Gets the value of the 'packets' field.
   * @return The value of the 'packets' field.
   */
  public java.lang.Long getPackets() {
    return packets;
  }

  /**
   * Sets the value of the 'packets' field.
   * @param value the value to set.
   */
  public void setPackets(java.lang.Long value) {
    this.packets = value;
  }

  /**
   * Gets the value of the 'tcpFlags' field.
   * @return The value of the 'tcpFlags' field.
   */
  public java.util.Map<java.lang.CharSequence,java.lang.Long> getTcpFlags() {
    return tcpFlags;
  }

  /**
   * Sets the value of the 'tcpFlags' field.
   * @param value the value to set.
   */
  public void setTcpFlags(java.util.Map<java.lang.CharSequence,java.lang.Long> value) {
    this.tcpFlags = value;
  }

  /**
   * Creates a new Metrics RecordBuilder.
   * @return A new Metrics RecordBuilder
   */
  public static com.fakecompany.metrics.Metrics.Builder newBuilder() {
    return new com.fakecompany.metrics.Metrics.Builder();
  }

  /**
   * Creates a new Metrics RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Metrics RecordBuilder
   */
  public static com.fakecompany.metrics.Metrics.Builder newBuilder(com.fakecompany.metrics.Metrics.Builder other) {
    return new com.fakecompany.metrics.Metrics.Builder(other);
  }

  /**
   * Creates a new Metrics RecordBuilder by copying an existing Metrics instance.
   * @param other The existing instance to copy.
   * @return A new Metrics RecordBuilder
   */
  public static com.fakecompany.metrics.Metrics.Builder newBuilder(com.fakecompany.metrics.Metrics other) {
    return new com.fakecompany.metrics.Metrics.Builder(other);
  }

  /**
   * RecordBuilder for Metrics instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Metrics>
    implements org.apache.avro.data.RecordBuilder<Metrics> {

    private long addr;
    private long octets;
    private long packets;
    private java.util.Map<java.lang.CharSequence,java.lang.Long> tcpFlags;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.fakecompany.metrics.Metrics.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.addr)) {
        this.addr = data().deepCopy(fields()[0].schema(), other.addr);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.octets)) {
        this.octets = data().deepCopy(fields()[1].schema(), other.octets);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.packets)) {
        this.packets = data().deepCopy(fields()[2].schema(), other.packets);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.tcpFlags)) {
        this.tcpFlags = data().deepCopy(fields()[3].schema(), other.tcpFlags);
        fieldSetFlags()[3] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing Metrics instance
     * @param other The existing instance to copy.
     */
    private Builder(com.fakecompany.metrics.Metrics other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.addr)) {
        this.addr = data().deepCopy(fields()[0].schema(), other.addr);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.octets)) {
        this.octets = data().deepCopy(fields()[1].schema(), other.octets);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.packets)) {
        this.packets = data().deepCopy(fields()[2].schema(), other.packets);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.tcpFlags)) {
        this.tcpFlags = data().deepCopy(fields()[3].schema(), other.tcpFlags);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'addr' field.
      * @return The value.
      */
    public java.lang.Long getAddr() {
      return addr;
    }

    /**
      * Sets the value of the 'addr' field.
      * @param value The value of 'addr'.
      * @return This builder.
      */
    public com.fakecompany.metrics.Metrics.Builder setAddr(long value) {
      validate(fields()[0], value);
      this.addr = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'addr' field has been set.
      * @return True if the 'addr' field has been set, false otherwise.
      */
    public boolean hasAddr() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'addr' field.
      * @return This builder.
      */
    public com.fakecompany.metrics.Metrics.Builder clearAddr() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'octets' field.
      * @return The value.
      */
    public java.lang.Long getOctets() {
      return octets;
    }

    /**
      * Sets the value of the 'octets' field.
      * @param value The value of 'octets'.
      * @return This builder.
      */
    public com.fakecompany.metrics.Metrics.Builder setOctets(long value) {
      validate(fields()[1], value);
      this.octets = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'octets' field has been set.
      * @return True if the 'octets' field has been set, false otherwise.
      */
    public boolean hasOctets() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'octets' field.
      * @return This builder.
      */
    public com.fakecompany.metrics.Metrics.Builder clearOctets() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'packets' field.
      * @return The value.
      */
    public java.lang.Long getPackets() {
      return packets;
    }

    /**
      * Sets the value of the 'packets' field.
      * @param value The value of 'packets'.
      * @return This builder.
      */
    public com.fakecompany.metrics.Metrics.Builder setPackets(long value) {
      validate(fields()[2], value);
      this.packets = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'packets' field has been set.
      * @return True if the 'packets' field has been set, false otherwise.
      */
    public boolean hasPackets() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'packets' field.
      * @return This builder.
      */
    public com.fakecompany.metrics.Metrics.Builder clearPackets() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'tcpFlags' field.
      * @return The value.
      */
    public java.util.Map<java.lang.CharSequence,java.lang.Long> getTcpFlags() {
      return tcpFlags;
    }

    /**
      * Sets the value of the 'tcpFlags' field.
      * @param value The value of 'tcpFlags'.
      * @return This builder.
      */
    public com.fakecompany.metrics.Metrics.Builder setTcpFlags(java.util.Map<java.lang.CharSequence,java.lang.Long> value) {
      validate(fields()[3], value);
      this.tcpFlags = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'tcpFlags' field has been set.
      * @return True if the 'tcpFlags' field has been set, false otherwise.
      */
    public boolean hasTcpFlags() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'tcpFlags' field.
      * @return This builder.
      */
    public com.fakecompany.metrics.Metrics.Builder clearTcpFlags() {
      tcpFlags = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Metrics build() {
      try {
        Metrics record = new Metrics();
        record.addr = fieldSetFlags()[0] ? this.addr : (java.lang.Long) defaultValue(fields()[0]);
        record.octets = fieldSetFlags()[1] ? this.octets : (java.lang.Long) defaultValue(fields()[1]);
        record.packets = fieldSetFlags()[2] ? this.packets : (java.lang.Long) defaultValue(fields()[2]);
        record.tcpFlags = fieldSetFlags()[3] ? this.tcpFlags : (java.util.Map<java.lang.CharSequence,java.lang.Long>) defaultValue(fields()[3]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Metrics>
    WRITER$ = (org.apache.avro.io.DatumWriter<Metrics>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Metrics>
    READER$ = (org.apache.avro.io.DatumReader<Metrics>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
