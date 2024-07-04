package com.lavsurgut.vertex;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Blob;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.api.GoogleSearchRetrieval;
import com.google.cloud.vertexai.api.Tool;
import com.google.cloud.vertexai.api.VertexAISearch;
import com.google.cloud.vertexai.api.Retrieval;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.google.protobuf.ByteString;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Objects;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiModal {
  public static String run() throws IOException {
    try (VertexAI vertexAi = new VertexAI("genaibuilders24ber-5354", "us-central1"); ) {
      List<Tool> tools = new ArrayList<>();
      tools.add(
          Tool.newBuilder()
              .setGoogleSearchRetrieval(
                  GoogleSearchRetrieval.newBuilder().setDisableAttribution(false))
              .build());
      GenerationConfig generationConfig =
          GenerationConfig.newBuilder()
              .setMaxOutputTokens(8192)
              .setTopP(0.95F)
              .build();
      List<SafetySetting> safetySettings = Arrays.asList(
        SafetySetting.newBuilder()
            .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
            .build(),
        SafetySetting.newBuilder()
            .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
            .build(),
        SafetySetting.newBuilder()
            .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
            .build(),
        SafetySetting.newBuilder()
            .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
            .build()
      );
      var systemInstruction = ContentMaker.fromMultiModalData("You are Clojure O´Doyle Rules Engine Expert. You will be given a task in natural language. Translate it to valid Clojure O´Doyle Rules. For example:  Given a query like: \"Move the player's position based on its current position\", produce valid o´doyle rules in Clojure: \n (def rules \n (o/ruleset \n {::player \n[:what \n [::player ::x x] \n [::player ::y y]] \n::move-player \n[:what \n[::time   ::delta dt] \n[::player ::x     x {:then false}] ;; don't run the :then block if only this is updated! \n :then \n (o/insert! ::player ::x (+ x dt))]}))");
      GenerativeModel model =
        new GenerativeModel.Builder()
            .setModelName("gemini-1.5-flash-001")
            .setVertexAi(vertexAi)
            .setGenerationConfig(generationConfig)
            .setSafetySettings(safetySettings)
            .setTools(tools)
            .setSystemInstruction(systemInstruction)
            .build();


      var content = ContentMaker.fromMultiModalData("If a users sees an article about merch in the last 10 minutes mark them as \"Merchandise Fanatic\"");
      ResponseStream<GenerateContentResponse> responseStream = model.generateContentStream(content);

      // Do something with the response
      responseStream.stream().forEach(System.out::println);

      String output =
      responseStream.stream()
        .map(Objects::toString)
        .collect(Collectors.joining(","));
        
      return output;
    }
  }
}
