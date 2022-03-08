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

import org.springframework.http.MediaType;
import se.callista.blog.avro_spring.serde.AvroConstants;

/**
 * Avro HTTP message converter.
 *
 * @author Björn Beskow
 */
public class AvroBinaryHttpMessageConverter<T> extends AvroHttpMessageConverter<T> {

  public AvroBinaryHttpMessageConverter() {
    super(true, new MediaType(AvroConstants.MEDIA_TYPE, AvroConstants.MEDIA_SUBTYPE_AVRO_BINARY, DEFAULT_CHARSET));
  }

}
