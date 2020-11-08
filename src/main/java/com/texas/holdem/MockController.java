package com.texas.holdem;

import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/socket.io")
    public SomeResponse mock3(){
        var xD= "xD";
        return new SomeResponse(xD);
    }
    @PostMapping("/socket.io")
    public SomeResponse mock4(){
        var xD= "xD";
        return new SomeResponse(xD);
    }
    @PutMapping("/socket.io")
    public SomeResponse mock5(){
        var xD= "xD";
        return new SomeResponse(xD);
    }
    @DeleteMapping("/socket.io")
    public SomeResponse mock6(){
        var xD= "xD";
        return new SomeResponse(xD);
    }
}
