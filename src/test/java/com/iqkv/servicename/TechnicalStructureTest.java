/*
 * Copyright 2025 IQKV Foundation Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.servicename;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.belongToAnyOf;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = ServicenameApplication.class, importOptions = DoNotIncludeTests.class)
class TechnicalStructureTest {

  // prettier-ignore
  @ArchTest
  static final ArchRule respectsTechnicalArchitectureLayers = layeredArchitecture()
      .consideringAllDependencies()
      .optionalLayer("Config").definedBy("..config..")
      .optionalLayer("Web").definedBy("..web..")
      .optionalLayer("Service").definedBy("..service..")
      .optionalLayer("Security").definedBy("..security..")
      .optionalLayer("Persistence").definedBy("..repository..")
      .optionalLayer("Domain").definedBy("..domain..")

      .whereLayer("Config").mayNotBeAccessedByAnyLayer()
      .whereLayer("Web").mayOnlyBeAccessedByLayers("Config")
      .whereLayer("Service").mayOnlyBeAccessedByLayers("Web", "Config")
      .whereLayer("Security").mayOnlyBeAccessedByLayers("Config", "Service", "Web")
      .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service", "Security", "Web", "Config")
      .whereLayer("Domain").mayOnlyBeAccessedByLayers("Persistence", "Service", "Security", "Web", "Config")

      .ignoreDependency(belongToAnyOf(ServicenameApplication.class), alwaysTrue());
}
