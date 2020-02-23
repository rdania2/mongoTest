package com.blazemeter.demo.config;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfiguration {

	private final static Logger log = LoggerFactory.getLogger(MongoConfiguration.class);

	private final MongoTemplate mongoTemplate;
	private final MongoConverter mongoConverter;

	@Autowired
	MongoConfiguration(MongoTemplate mongoTemplate, MongoConverter mongoConverter){
		this.mongoConverter = mongoConverter;
		this.mongoTemplate = mongoTemplate;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initIndicesAfterStartup() {

		log.info("Mongo InitIndicesAfterStartup init");
		var init = System.currentTimeMillis();

		var mappingContext = this.mongoConverter.getMappingContext();

		if (mappingContext instanceof MongoMappingContext) {
			MongoMappingContext mongoMappingContext = (MongoMappingContext) mappingContext;
			for (BasicMongoPersistentEntity<?> persistentEntity : mongoMappingContext.getPersistentEntities()) {
				var clazz = persistentEntity.getType();
				if (clazz.isAnnotationPresent(Document.class)) {
					var resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);

					var indexOps = this.mongoTemplate.indexOps(clazz);
					resolver.resolveIndexFor(clazz).forEach(indexOps::ensureIndex);
				}
			}
		}

		log.info("Mongo InitIndicesAfterStartup take: {}", (System.currentTimeMillis() - init));
	}

}
