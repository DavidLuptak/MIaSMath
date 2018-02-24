/*
 * Copyright 2016 MIR@MU Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.muni.fi.mias;

import cz.muni.fi.mias.math.MathTokenizer;
import java.io.Reader;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;

/**
 * Factory used for calling MathTokenizer from SOLR environment. The following
 * attributes must be specified in the schema.xml file for tokenizer
 * MathTokenizer:
 * <ul>
 * <li>
 * subformulae – true for analyzer type index, false for analyzer type query
 * </li>
 * </ul>
 *
 * Complete example:
 * <pre>{@code
 * <fieldType name="math" class="solr.TextField">
 *   <analyzer type="index">
 *     <tokenizer class="cz.muni.fi.mias.MathTokenizerFactory" subformulae="true"  />
 *   </analyzer>
 *   <analyzer type="query">
 *     <tokenizer class="cz.muni.fi.mias.MathTokenizerFactory" subformulae="false" />
 *   </analyzer>
 * </fieldType>
 * }</pre>
 *
 * @author Martin Liska
 */
public class MathTokenizerFactory extends AbstractTokenizerFactory {

    private static final Logger LOG = LogManager.getLogger(MathTokenizerFactory.class);

    private final boolean subformulae;

// Lucene leftover
//    public MathTokenizerFactory(Map<String, String> args) {
//        super(args);
//        String subforms = args.get("subformulae");
//        subformulae = Boolean.parseBoolean(subforms);
//    }

    // ES constructor
    public MathTokenizerFactory(IndexSettings indexSettings, String name, Settings settings) {
        super(indexSettings, name, settings);
        this.subformulae = true;
    }

    // Lucene leftover
    // @Override
    public Tokenizer create(AttributeFactory af, Reader reader) {
        LOG.info("Original Lucene Tokenizer create method called.");
        return new MathTokenizer(reader, subformulae, MathTokenizer.MathMLType.BOTH);
    }

    // ES purposes
    public static MathTokenizerFactory getMathTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new MathTokenizerFactory(indexSettings, name, settings);
    }

    @Override
    public Tokenizer create() {
        return new MathTokenizer();
    }
}
