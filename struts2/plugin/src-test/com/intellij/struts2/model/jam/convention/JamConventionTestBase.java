/*
 * Copyright 2010 The authors
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.struts2.model.jam.convention;

import com.intellij.jam.JamElement;
import com.intellij.jam.reflect.JamClassMeta;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.struts2.BasicHighlightingTestCase;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for Convention JAM tests.
 *
 * @author Yann C&eacute;bron
 */
abstract class JamConventionTestBase<T extends JavaModuleFixtureBuilder> extends BasicHighlightingTestCase<T> {

  @Override
  @NotNull
  protected String getTestDataLocation() {
    return "model/jam/convention/" + getTestDataFolder();
  }

  @Override
  protected boolean hasJavaSources() {
    return true;
  }

  /**
   * Returns the test data folder name located relative to {@code model/jam/convention}.
   *
   * @return Folder name.
   */
  @NonNls
  @NotNull
  protected abstract String getTestDataFolder();

  /**
   * Perform additional configuration.
   *
   * @param moduleBuilder Instance.
   * @throws Exception On errors.
   */
  protected void postConfigureModule(final T moduleBuilder) throws Exception {
  }

  @Override
  protected void configureModule(final T moduleBuilder) throws Exception {
    super.configureModule(moduleBuilder);
    addLibrary(moduleBuilder, "struts2-convention-plugin", "struts2-convention-plugin-2.2.1.jar");
    postConfigureModule(moduleBuilder);
  }

  /**
   * Gets the JAM-element for the given class.
   *
   * @param clazzName FQN of class.
   * @param meta      JAM-Meta.
   * @param <Jam>     JAM-Type.
   * @return JAM.
   * @throws Exception On errors.
   */
  @NotNull
  protected <Jam extends JamElement> Jam getClassJam(final String clazzName,
                                                     final JamClassMeta<Jam> meta) throws Exception {
    myFixture.configureByFile(SOURCE_PATH + "/" + StringUtil.replace(clazzName, ".", "/") + ".java");

    final PsiClass myClass = JavaPsiFacade.getInstance(myProject)
                                          .findClass(clazzName, GlobalSearchScope.moduleScope(myModule));
    assertNotNull("could not find class '" + clazzName + "'", myClass);

    final Jam jam = meta.getJamElement(myClass);
    assertNotNull("JAM was null for " + meta + " in '" + clazzName + "'", jam);
    return jam;
  }

}