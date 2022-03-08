package se.callista.blog.avro_spring.serde;

/**
 * Constants.
 */
public class AvroConstants {

  public static final String MEDIA_TYPE = "application";
  public static final String MEDIA_SUBTYPE_AVRO_JSON = "avro+json";
  public static final String MEDIA_SUBTYPE_AVRO_BINARY = "avro";
  public static final String MEDIA_TYPE_AVRO_JSON = MEDIA_TYPE + "/" + MEDIA_SUBTYPE_AVRO_JSON;
  public static final String MEDIA_TYPE_AVRO_BINARY = MEDIA_TYPE + "/" + MEDIA_SUBTYPE_AVRO_BINARY;
}
