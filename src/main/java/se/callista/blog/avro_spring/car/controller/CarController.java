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

package se.callista.blog.avro_spring.car.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.callista.blog.avro_spring.car.avro.Car;
import se.callista.blog.avro_spring.car.persist.CarRepository;
import se.callista.blog.avro_spring.serde.AvroConstants;

/**
 * Rest Controller example, using Avro Serializer/Deserializer.
 * 
 * @author Björn Beskow
 */
@RestController
public class CarController {

  private final CarRepository repository;

  public CarController(CarRepository repository) {
    this.repository = repository;
  }

  @RequestMapping(value = "/car/{VIN}", method = RequestMethod.GET,
      produces = {AvroConstants.MEDIA_TYPE_AVRO_JSON, AvroConstants.MEDIA_TYPE_AVRO_BINARY})
  public Car getCar(@PathVariable("VIN") String VIN) {
    return repository.getCar(VIN);
  }

  @RequestMapping(value = "/car/{VIN}", method = RequestMethod.PUT,
      consumes = {AvroConstants.MEDIA_TYPE_AVRO_JSON, AvroConstants.MEDIA_TYPE_AVRO_BINARY},
      produces = {AvroConstants.MEDIA_TYPE_AVRO_JSON, AvroConstants.MEDIA_TYPE_AVRO_BINARY})
  public Car updateCar(@PathVariable("VIN") String VIN, @RequestBody Car car) {
    repository.updateCar(car);
    return car;
  }

}
