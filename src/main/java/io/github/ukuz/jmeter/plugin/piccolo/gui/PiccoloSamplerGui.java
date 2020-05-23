/*
 * Copyright 2020 ukuz90
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
package io.github.ukuz.jmeter.plugin.piccolo.gui;

import io.github.ukuz.jmeter.plugin.piccolo.PiccoloSampler;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

/**
 * @author ukuz90
 */
@Slf4j
public class PiccoloSamplerGui extends AbstractSamplerGui {

    private PiccoloGui panel;

    public PiccoloSamplerGui() {
        this.panel = new PiccoloControlPanel();
        init();
    }

    private void init() {
        JPanel settingPanel = new VerticalPanel(5, 0);
        settingPanel.setBorder(makeBorder());
        Container container = makeTitlePanel();
        settingPanel.add(container);
        panel.drawOn(settingPanel);

        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(settingPanel, BorderLayout.CENTER);
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public TestElement createTestElement() {
        PiccoloSampler sampler = new PiccoloSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {
        super.configureTestElement(testElement);
        panel.flushData(testElement);
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        panel.configure(element);
    }

    @Override
    public String getStaticLabel() {
        return "Piccolo Sampler";
    }

    @Override
    public void clearGui() {
        super.clearGui();
        panel.clear();
    }
}
