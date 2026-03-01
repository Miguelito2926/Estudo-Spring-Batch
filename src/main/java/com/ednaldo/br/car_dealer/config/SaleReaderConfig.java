package com.ednaldo.br.car_dealer.config;

import com.ednaldo.br.car_dealer.domain.SaleRecord;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.RecordFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

@Configuration
public class SaleReaderConfig {

    @Bean
    @StepScope
    public MultiResourceItemReader<SaleRecord> saleReader(
            ResourcePatternResolver resolver,
            FlatFileItemReader<SaleRecord> saleFileReader,
            @Value("${app.filial-report-pattern}") String pattern) throws IOException, IOException {
        Resource[] resources = resolver.getResources(pattern);
        Arrays.sort(resources, Comparator.comparing(Resource::getFilename));
        MultiResourceItemReader<SaleRecord> reader = new MultiResourceItemReader<>(saleFileReader);
        reader.setResources(resources);
        return reader;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<SaleRecord> saleFileReader() {
        RecordFieldSetMapper<SaleRecord> mapper = new RecordFieldSetMapper<>(SaleRecord.class);
        return new FlatFileItemReaderBuilder<SaleRecord>()
                .name("saleFileReader")
                .linesToSkip(1)
                .delimited()
                .names("dealerId", "saleDate", "model", "paymentType", "salePriceBrl")
                .fieldSetMapper(mapper)
                .build();
    }
}