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

import bsh.StringUtil;
import io.github.ukuz.jmeter.plugin.piccolo.Constant;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;

/**
 * @author ukuz90
 */
public class PiccoloControlPanel implements PiccoloGui {

    private JTextField addressText;
    public static final int TEXT_COLUMNS = 4;

    @Override
    public void clear() {

    }

    @Override
    public void drawOn(JComponent component) {
        component.add(makePiccoloWSSettingPanel());
    }

    @Override
    public void flushData(TestElement element) {
        String url = addressText.getText();
        Constant.setWebSocketUrl(element, url);
    }

    @Override
    public void configure(TestElement element) {
        addressText.setText(Constant.getWebSocketUrl(element));
    }

    public JPanel makePiccoloWSSettingPanel() {
        JPanel wsSetting = new VerticalPanel();
        wsSetting.setBorder(BorderFactory.createTitledBorder("Piccolo 网关配置"));

        //Address
        JPanel ah = new HorizontalPanel();
        JLabel addressLable = new JLabel("URL:", SwingConstants.RIGHT);
        addressText = new JTextField(TEXT_COLUMNS);
        addressLable.setLabelFor(addressText);
        ah.add(addressLable);
        ah.add(addressText);
        ah.add(makeHelper("Use the registry to allow multiple addresses, Use direct connection to allow only one address! Multiple address format: ip1:port1,ip2:port2 . Direct address format: ip:port . "));

        wsSetting.add(ah);

        return wsSetting;
    }

    public JLabel makeHelper(String tooltip) {
        JLabel helpLable = new JLabel();
        helpLable.setIcon(new ImageIcon(getClass().getResource("/images/help.png")));
        helpLable.setToolTipText(tooltip);
        return helpLable;
    }
}
