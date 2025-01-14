package org.mule.extension.mulechain.internal.streaming;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.extension.mulechain.internal.llm.LangchainLLMConfiguration;
import org.mule.extension.mulechain.internal.llm.LangchainLLMParameters;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.Streaming;

import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import static java.time.Duration.ofSeconds;



/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class LangchainLLMStreamingOperations {

	
	

  
  /*   
   * https://docs.mulesoft.com/mule-sdk/latest/define-operations
   * Define output resolver
   *  */
  interface Assistant {

      TokenStream chat(String message);
  }
  
  @MediaType(value = ANY, strict = false)
  @Alias("Stream-prompt-answer")  
  @OutputResolver(output = TokenStreamOutputResolver.class)
  @Streaming
  public TokenStream streamingPrompt(String prompt, @Config LangchainLLMConfiguration configuration, @ParameterGroup(name= "Additional properties") LangchainLLMParameters LangchainParams) {

      StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
              .apiKey(System.getenv("OPENAI_API_KEY").replace("\n", "").replace("\r", ""))
              .modelName(LangchainParams.getModelName())
              .temperature(0.3)
              .timeout(ofSeconds(60))
              .logRequests(true)
              .logResponses(true)
              .build();


      Assistant assistant = AiServices.create(Assistant.class, model);


      TokenStream tokenStream = assistant.chat(prompt);


          tokenStream.onNext(System.out::println)
                .onComplete(System.out::println)
                .onError(Throwable::printStackTrace)
         .start();


        return tokenStream;

	  
  }
}
