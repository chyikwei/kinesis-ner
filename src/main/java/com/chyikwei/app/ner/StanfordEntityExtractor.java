package com.chyikwei.app.ner;

import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class StanfordEntityExtractor implements EntityExtractInterface{
  public static final String NO_ENTITY = "O";
  private StanfordCoreNLP pipeline;
  private String entityType;
  private StringBuilder tokenBuilder;
  private static StanfordEntityExtractor instance;

  private StanfordEntityExtractor() {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
    pipeline = new StanfordCoreNLP(props);
    tokenBuilder = new StringBuilder();
  }

  public static StanfordEntityExtractor getInstance(){
    if (instance == null) {
      instance = new StanfordEntityExtractor();
    }
    return instance;
  }


  public List<Pair<String, String>> annotate(String text) {
    Annotation document = new Annotation(text);
    pipeline.annotate(document);
    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

    List<Pair<String, String>> entities = new ArrayList<Pair<String, String>>();
    for (CoreMap sentence : sentences) {
      // reset token tracking
      resetEntityTracker();

      for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
        // this is the text of the token
        String word = token.get(TextAnnotation.class);
        // this is the NER label of the token
        String ne = token.get(NamedEntityTagAnnotation.class);

        // no entity for current token
        if (NO_ENTITY.equals(ne)) {
          if (entityType != null) {
            // if current entity is not null, add to list
            entities.add(currentEntity());
            resetEntityTracker();
          }
        } else {
          // current token is entity
          if (entityType == null) {
            // first token
            entityType = ne;
            tokenBuilder.append(word);
          } else if (entityType.equals(ne)) {
            // same entity type, append token
            tokenBuilder.append(" " + word);
          } else {
            // different entity type
            entities.add(currentEntity());
            resetEntityTracker();
            // add current token
            entityType = ne;
            tokenBuilder.append(word);
          }
        }
      }
    }
    // last token
    if (entityType != null) {
      entities.add(currentEntity());
      resetEntityTracker();
    }
    return entities;
  }

  private void resetEntityTracker() {
    entityType = null;
    tokenBuilder.setLength(0);
  }

  private Pair<String, String> currentEntity() {
    return ImmutablePair.of(entityType, tokenBuilder.toString());
  }
}