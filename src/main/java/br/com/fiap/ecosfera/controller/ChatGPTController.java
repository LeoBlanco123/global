package br.com.fiap.ecosfera.controller;

import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatGPTController {

    @Autowired
    private OpenAiChatClient openAiChatClient;

    @GetMapping("/form_chat_gpt")
    public String retornaFormChatGPT() {
        return "form_chat_gpt";
    }

    @PostMapping("/enviar_pergunta_chat_gpt")
    public String enviarPerguntaChatGPT(@RequestParam(name = "pergunta") String pergunta, Model model) {
        String resposta = openAiChatClient.call(pergunta);
        model.addAttribute("var", resposta);
        return "form_chat_gpt";
    }
}
