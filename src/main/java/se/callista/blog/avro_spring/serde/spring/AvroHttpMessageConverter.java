/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.callista.blog.avro_spring.serde.spring;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import se.callista.blog.avro_spring.serde.AvroDeserializer;
import se.callista.blog.avro_spring.serde.AvroSerializer;
import se.callista.blog.avro_spring.serde.Deserializer;
import se.callista.blog.avro_spring.serde.Serializer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Avro HTTP message converter.
 * 
 * @author Björn Beskow
 */
public abstract class AvroHttpMessageConverter<T> extends AbstractHttpMessageConverter<T> {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private final Serializer<SpecificRecordBase> serializer;
  private final Deserializer<SpecificRecordBase> deserializer;

  public AvroHttpMessageConverter(boolean useBinaryEncoding, MediaType... supportedMediaTypes) {
    super(supportedMediaTypes);
    serializer = new AvroSerializer<>(useBinaryEncoding);
    deserializer = new AvroDeserializer<>(useBinaryEncoding);
  }

  @Override
  protected boolean supports(Class<?> clazz) {
    return SpecificRecordBase.class.isAssignableFrom(clazz);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage)
      throws IOException, HttpMessageNotReadableException {
    T result = null;
    byte[] data = IOUtils.toByteArray(inputMessage.getBody());
    if (data.length > 0) {
      result = (T) deserializer.deserialize((Class<? extends SpecificRecordBase>) clazz, data);
    }
    return result;
  }

  @Override
  protected void writeInternal(T t, HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
    byte[] data = serializer.serialize((SpecificRecordBase) t);
    outputMessage.getBody().write(data);
  }

}
