package com.edu.tcc.carbon.carbon.handlers;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder
                // Modulos para lidar com JSR310 (Java 8 Data/Time API)
                .modules(new JavaTimeModule())
                // Desabilita a escrita de datas como timestamps
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                
                // --- Configurações que podem forçar a falha antecipada ---
                
                // Faz o Jackson falhar se encontrar uma propriedade no JSON que não existe no DTO
                // Útil para evitar payloads com dados inesperados.
                // Esta feature é para propriedades *desconhecidas*, não para *ausentes*
                // builder.featuresToEnable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // Removido para não atrapalhar, se não for o objetivo

                // MUITO IMPORTANTE: Isso instrui o Jackson a falhar se uma propriedade for `null`
                // para um tipo que não permite `null` (ex: `int`, `boolean`).
                // Para campos de referência (String, objetos), `null` é geralmente permitido.
                // Contudo, se `required=true` + `@NotBlank` estão falhando com `null`,
                // pode ser que esta feature force a exceção mais cedo.
                // CUIDADO: Pode ter efeitos colaterais se outros campos puderem ser nulos.
                .featuresToEnable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES); // <--- Mantenha isso ativo
                
            // Post-configurer para o ObjectMapper final
            builder.postConfigurer(mapper -> {
                // Sua configuração de coerção textual
                mapper.coercionConfigFor(LogicalType.Textual)
                    .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail)
                    .setCoercion(CoercionInputShape.Float, CoercionAction.Fail);

                // Podemos tentar habilitar esta feature diretamente no mapper para ser mais explícito
                // mapper.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PROPERTIES); // Redundante se já no builder

                // Para ser extremamente rigoroso com tipos primitivos (int, boolean etc.)
                // mapper.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            });
        };
    }
}