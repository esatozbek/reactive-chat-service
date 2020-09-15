package controller;

import dto.MessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import response.IdResponse;
import service.MessageService;

@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {
    private MessageService messageService;

    @GetMapping("/{id}")
    public Mono<MessageDTO> getMessage(@PathVariable Long id) {
        return Mono.just(messageService.findById(id));
    }

    @GetMapping()
    public Flux<MessageDTO> getAllMessages() {
        return Flux.fromIterable(messageService.findAll());
    }

    @PutMapping("/{id}")
    public Mono<IdResponse> updateMessage(@PathVariable Long id, @RequestBody MessageDTO messageDTO) {
        return Mono.just(new IdResponse(messageService.update(id, messageDTO)));
    }

    @PostMapping()
    public Mono<IdResponse> createMessage(@RequestBody MessageDTO messageDTO) {
        return Mono.just(new IdResponse(messageService.create(messageDTO)));
    }

    @DeleteMapping("/{id}")
    public Mono<IdResponse> deleteMessage(@PathVariable Long id) {
        return Mono.just(new IdResponse(messageService.delete(id)));
    }
}
