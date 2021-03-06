package com.chyikwei.app.ner;

import com.chyikwei.app.model.Entity;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

public class StanfordEntityExtractor implements EntityExtractor {
  public static final String NO_ENTITY = "O";
  private StanfordCoreNLP pipeline;
  private String entityType;
  private StringBuilder tokenBuilder;
  private static StanfordEntityExtractor instance;

  private StanfordEntityExtractor() {
    Properties props = new Properties();
    // NER pipeline reqired the following steps
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
    pipeline = new StanfordCoreNLP(props);
    tokenBuilder = new StringBuilder();
  }

  /**
   * get StanfordEntityExtractor instance
   *
   * @return StanfordEntityExtractor
   */
  public static StanfordEntityExtractor getInstance(){
    if (instance == null) {
      instance = new StanfordEntityExtractor();
    }
    return instance;
  }

  /**
   * Annotate input and return a list of entities in the text
   *
   * @param text
   * @return
   */
  @Override
  public List<Entity> annotate(String text) {
    Annotation document = new Annotation(text);
    pipeline.annotate(document);
    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

    List<Entity> entities = new ArrayList<>();
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

  private Entity currentEntity() {
    return new Entity(entityType, tokenBuilder.toString());
  }
}