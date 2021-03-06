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

package com.intellij.struts2.facet.ui;

import com.intellij.facet.Facet;
import com.intellij.facet.frameworks.LibrariesDownloadAssistant;
import com.intellij.facet.frameworks.beans.Artifact;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.libraries.FacetLibrariesValidator;
import com.intellij.facet.ui.libraries.LibraryInfo;
import com.intellij.openapi.module.Module;
import com.intellij.struts2.StrutsBundle;
import com.intellij.struts2.facet.StrutsFacetConfiguration;
import com.intellij.struts2.facet.StrutsFacetLibrariesValidatorDescription;
import com.intellij.struts2.facet.StrutsFrameworkSupportProvider;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Struts2 facet tab "Features".
 *
 * @author Yann C&eacute;bron
 */
public class FeaturesConfigurationTab extends FacetEditorTab {

  private JPanel myPanel;
  private JComboBox versionComboBox;
  private JCheckBox disablePropertiesKeys;

  private final StrutsFacetConfiguration originalConfiguration;
  private final FacetLibrariesValidator validator;

  public FeaturesConfigurationTab(final StrutsFacetConfiguration originalConfiguration,
                                  final FacetEditorContext editorContext,
                                  final FacetLibrariesValidator validator) {
    this.originalConfiguration = originalConfiguration;
    this.validator = validator;

    disablePropertiesKeys.setSelected(originalConfiguration.isPropertiesKeysDisabled());

    versionComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        final Artifact version = getSelectedVersion();
        if (version != null) {
          validator.setRequiredLibraries(getRequiredLibraries());
          validator.setDescription(new StrutsFacetLibrariesValidatorDescription(version.getVersion()));
        }
      }
    });
    final Module module = editorContext.getModule();
    final String version = StrutsVersionDetector.detectStrutsVersion(module);
    if (version != null) {
      versionComboBox.setModel(new DefaultComboBoxModel(new String[]{version}));
      versionComboBox.getModel().setSelectedItem(version);
      versionComboBox.setEnabled(false);
      return;
    }

    final Artifact[] versions = LibrariesDownloadAssistant.getVersions(StrutsFrameworkSupportProvider.getLibrariesUrl());
    versionComboBox.setModel(new DefaultComboBoxModel(versions));
    if (versions.length > 0) {
      final Artifact item = versions[0];
      versionComboBox.getModel().setSelectedItem(item);
      validator.setRequiredLibraries(getRequiredLibraries());
      validator.setDescription(new StrutsFacetLibrariesValidatorDescription(item.getVersion()));
    }
  }


  @Nullable
  private Artifact getSelectedVersion() {
    final Object version = versionComboBox.getModel().getSelectedItem();
    return version instanceof Artifact ? (Artifact) version : null;
  }

  @Nullable
  private LibraryInfo[] getRequiredLibraries() {
    final Artifact version = getSelectedVersion();

    return version == null ? null : LibrariesDownloadAssistant.getLibraryInfos(version);
  }

  public void onFacetInitialized(@NotNull final Facet facet) {
    validator.onFacetInitialized(facet);
  }

  @Nullable
  public Icon getIcon() {
    return PlatformIcons.TASK_ICON;
  }

  @Nls
  public String getDisplayName() {
    return StrutsBundle.message("facet.features.title");
  }

  public JComponent createComponent() {
    return myPanel;
  }

  public boolean isModified() {
    return originalConfiguration.isPropertiesKeysDisabled() !=
           disablePropertiesKeys.isSelected();
  }

  public void apply() {
    originalConfiguration.setPropertiesKeysDisabled(disablePropertiesKeys.isSelected());
    originalConfiguration.setModified();
  }

  public void reset() {
  }

  public void disposeUIResources() {
  }

  @Override
  public String getHelpTopic() {
    return "reference.settings.project.structure.facets.struts2.facet";
  }
}