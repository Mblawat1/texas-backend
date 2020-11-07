package com.texas.holdem;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins="*")
@RestController
public class MockController {

    class SomeResponse{
        private String text;

        public SomeResponse(String text) {
            this.text = text;
        }

        public SomeResponse() {
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @GetMapping("/**/{[path:[^\\\\.]*}")
    public SomeResponse mock(){
        var xD= "xD";
        return new SomeResponse(xD);
    }

    @GetMapping("/")
    public SomeResponse mock2(){
        var xD= "xD";
        return new SomeResponse(xD);
    }
}
