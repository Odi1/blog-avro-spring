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

package se.callista.blog.avro_spring.car.conf;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import se.callista.blog.avro_spring.serde.spring.AvroBinaryHttpMessageConverter;
import se.callista.blog.avro_spring.serde.spring.AvroJsonHttpMessageConverter;

import java.util.List;

/**
 * Configuration required to use the Avro{Binary,Json}HttpMessageConverter.
 * 
 * @author Björn Beskow
 */
@Configuration
public class ConverterConfig implements WebMvcConfigurer {

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // This will add our message converters at the end of the list, with about 10 default converters in front of us.
    // Order matters: We may not use a media type which triggers any of the default converters, otherwise we will
    // never be called.

    // We need to add our message converters at the top of the list of about 10 default converters after us.
    // Order matters: If we were at the end of the list, we would be restricted in which media types we are allowed
    // to use. If e.g. we use "application/avro+json" and we were at the bottom of the list, then a Jackson message
    // converter will try to handle it.
    converters.add(0, new AvroBinaryHttpMessageConverter<SpecificRecordBase>());
    converters.add(0, new AvroJsonHttpMessageConverter<SpecificRecordBase>());
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    // We need to add the AvroJsonHttpMessageConverter before any generic JSON converter,
    // since the pattern a generic JSON converter also may match. 
    RestTemplate restTemplate = builder.build();
    restTemplate.getMessageConverters().add(0, new AvroJsonHttpMessageConverter<SpecificRecordBase>());
    restTemplate.getMessageConverters().add(0, new AvroBinaryHttpMessageConverter<SpecificRecordBase>());
    return restTemplate;
  }

}
