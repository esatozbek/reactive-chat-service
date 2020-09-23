package controller;

import dto.MessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import request.MessageRequest;
import response.IdResponse;
import service.MessageService;

@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {
    private MessageService messageService;

    @GetMapping("/{id}")
    public Mono<MessageDTO> getMessage(@PathVariable Long id) {
        return messageService.findById(id);
    }

    @GetMapping()
    public Flux<MessageDTO> getAllMessages() {
        return messageService.findAll();
    }

    @PutMapping("/{id}")
    public Mono<IdResponse> updateMessage(@PathVariable Long id, @RequestBody MessageRequest messageRequest) {
        return messageService.update(id, messageRequest)
                .map(IdResponse::new);
    }

    @PostMapping()
    public Mono<IdResponse> createMessage(@RequestBody MessageRequest request) {
        return messageService.create(request)
                .map(IdResponse::new);
    }

    @DeleteMapping("/{id}")
    public Mono<IdResponse> deleteMessage(@PathVariable Long id) {
        return messageService.delete(id)
                .then(Mono.just(new IdResponse(id)));
    }

    @GetMapping("/sender/{id}")
    public Flux<MessageDTO> getMessagesBySender(@PathVariable Long id) {
        return messageService.findMessagesBySender(id);
    }

    @GetMapping("/receiver/{id}")
    public Flux<MessageDTO> getMessagesByReceiver(@PathVariable Long id) {
        return messageService.findMessagesByReceiver(id);
    }

    @GetMapping("/group/{id}")
    public Flux<MessageDTO> getMessagesByGroup(@PathVariable Long id) {
        return messageService.findMessagesByGroupId(id);
    }

    @GetMapping("/content/{content}")
    public Flux<MessageDTO> getMessagesByContent(@PathVariable String content) {
        return messageService.findMessagesByContent(content);
    }

    @GetMapping("/start/{start}/end/{end}")
    public Flux<MessageDTO> getMessagesByTimestamps(@PathVariable Long start, @PathVariable Long end) {
        return messageService.findMessagesBetweenTimestamps(start, end);
    }
}
