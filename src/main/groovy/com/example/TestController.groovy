package com.example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
class TestController {

    @Autowired TestService service

    @RequestMapping(method = RequestMethod.POST)
    def create(@RequestParam String convert) {
        service.send(convert)
    }
}
