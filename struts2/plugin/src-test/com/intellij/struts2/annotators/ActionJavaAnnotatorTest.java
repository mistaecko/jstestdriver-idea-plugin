/*
 * Copyright 2011 The authors
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

package com.intellij.struts2.annotators;

import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.struts2.BasicHighlightingTestCase;
import com.intellij.util.Function;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Tests for {@link ActionJavaAnnotator}
 *
 * @author Yann C&eacute;bron
 */
public class ActionJavaAnnotatorTest extends BasicHighlightingTestCase {

  private final Function<PsiElement, String> ACTION_NAME_RESOLVE = new Function<PsiElement, String>() {
    @Override
    public String fun(final PsiElement psiElement) {
      return ((XmlTag) psiElement).getAttributeValue("name");
    }
  };

  @Override
  @NotNull
  protected String getTestDataLocation() {
    return "/gutterJava/actionClass";
  }

  @Override
  protected boolean hasJavaSources() {
    return true;
  }

  /**
   * Checks whether the gutter target elements in the given class resolve to the given names.
   *
   * @param javaFile            Path to class.
   * @param nameResolveFunction Naming function
   * @param expectedNames       Expected names.
   */
  private void checkGutterTargetElements(@NonNls final String javaFile,
                                         final Function<PsiElement, String> nameResolveFunction,
                                         @NonNls final String... expectedNames) {
    final GutterIconRenderer renderer = myFixture.findGutter(javaFile);
    assertNotNull(renderer);

    AnnotatorTestUtils.checkGutterTargets(renderer, nameResolveFunction, expectedNames);
  }

  @SkipStrutsLibrary
  public void testGutterMyAction() {
    createStrutsFileSet("struts-actionClass.xml");
    checkGutterTargetElements("/src/MyAction.java", ACTION_NAME_RESOLVE, "myActionPath");
  }

  @SkipStrutsLibrary
  public void testGutterMyActionMultipleMappings() {
    createStrutsFileSet("struts-actionClass-multiple_mappings.xml");
    checkGutterTargetElements("/src/MyAction.java", ACTION_NAME_RESOLVE,
                              "myActionPath1", "myActionPath2", "myActionPath3");
  }

  @SkipStrutsLibrary
  public void testGutterValidationXml() {
    createStrutsFileSet("struts-validation.xml");
    myFixture.copyFileToProject("/src/com/MyValidationAction-validation.xml");

    checkGutterTargetElements("/src/com/MyValidationAction.java", new Function<PsiElement, String>() {
      @Override
      public String fun(final PsiElement psiElement) {
        return ((PsiFile) psiElement).getName();
      }
    }, "MyValidationAction-validation.xml");
  }

}