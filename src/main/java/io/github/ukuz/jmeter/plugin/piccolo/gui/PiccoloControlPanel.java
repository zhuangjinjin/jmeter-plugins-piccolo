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
    private JTextField userIdPrefixText;
    private JTextField userIdText;
    private JTextField routeKeyText;
    private JTextField payloadFileText;
    public static final int TEXT_COLUMNS = 4;

    @Override
    public void clear() {

    }

    @Override
    public void drawOn(JComponent component) {
        component.add(makePiccoloWSSettingPanel());
        component.add(makePiccoloUserIdSettingPanel());
        component.add(makePiccoloDispatchMessagePanel());
    }

    @Override
    public void flushData(TestElement element) {
        Constant.setWebSocketUrl(element, addressText.getText());
        Constant.setBindUserIdPrefix(element, userIdPrefixText.getText());
        Constant.setBindUserId(element, userIdText.getText());
        Constant.setDispatchRouteKey(element, routeKeyText.getText());
        Constant.setDispatchPayloadFile(element, payloadFileText.getText());
    }

    @Override
    public void configure(TestElement element) {
        addressText.setText(Constant.getWebSocketUrl(element));
        userIdPrefixText.setText(Constant.getBindUserIdPrefix(element));
        userIdText.setText(Constant.getBindUserId(element));
        routeKeyText.setText(Constant.getDispatchRouteKey(element));
        payloadFileText.setText(Constant.getDispatchPayloadFile(element));
    }

    public JPanel makePiccoloWSSettingPanel() {
        JPanel wsSetting = new VerticalPanel();
        wsSetting.setBorder(BorderFactory.createTitledBorder("Piccolo 网关配置"));

        //Address
        JPanel ah = new HorizontalPanel();
        JLabel addressLabel = new JLabel("URL:", SwingConstants.RIGHT);
        addressText = new JTextField(TEXT_COLUMNS);
        addressLabel.setLabelFor(addressText);
        ah.add(addressLabel);
        ah.add(addressText);
        ah.add(makeHelper("address format: ws://ip:port"));

        wsSetting.add(ah);

        return wsSetting;
    }

    public JPanel makePiccoloUserIdSettingPanel() {
        JPanel userIdSetting = new VerticalPanel();
        userIdSetting.setBorder(BorderFactory.createTitledBorder("Piccolo 绑定用户ID配置"));

        JPanel uh = new HorizontalPanel();
        //UserIdPrefix
        JLabel userIdPrefixLabel = new JLabel("用户ID前缀:", SwingConstants.RIGHT);
        userIdPrefixText = new JTextField(TEXT_COLUMNS);
        userIdPrefixLabel.setLabelFor(userIdPrefixText);
        uh.add(userIdPrefixLabel);
        uh.add(userIdPrefixText);
        uh.add(makeHelper("userIdPrefix example: user_"));

        //UserId
        JLabel userIdLabel = new JLabel("用户ID:", SwingConstants.RIGHT);
        userIdText = new JTextField(TEXT_COLUMNS);
        userIdLabel.setLabelFor(userIdText);
        uh.add(userIdLabel);
        uh.add(userIdText);
        uh.add(makeHelper("userId example: user_1"));

        userIdSetting.add(uh);

        return userIdSetting;
    }

    public JPanel makePiccoloDispatchMessagePanel() {
        JPanel dispatchMessageSetting = new VerticalPanel();
        dispatchMessageSetting.setBorder(BorderFactory.createTitledBorder("Piccolo DispatchMessage配置"));

        JPanel dh = new VerticalPanel();

        //RouteKey
        JPanel rh = new HorizontalPanel();
        JLabel routeKeyLabel = new JLabel("RouteKey:", SwingConstants.RIGHT);
        routeKeyText = new JTextField(TEXT_COLUMNS);
        routeKeyLabel.setLabelFor(routeKeyText);
        rh.add(routeKeyLabel);
        rh.add(routeKeyText);
        dh.add(rh);

        //Payload
        JPanel ph = new HorizontalPanel();
        JLabel payloadLabel = new JLabel("Payload数据文件:", SwingConstants.RIGHT);
        payloadFileText = new JTextField();
        JButton payloadFileBtn = new JButton("选择文件");
        payloadLabel.setLabelFor(payloadFileText);
        ph.add(payloadLabel);
        ph.add(payloadFileText);
        ph.add(payloadFileBtn);
        ph.add(makeHelper("请选择payload数据文件"));
        payloadFileBtn.addActionListener(event -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = chooser.showOpenDialog(payloadFileBtn);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                payloadFileText.setText(filePath);
            }
        });
        dh.add(ph);

        dispatchMessageSetting.add(dh);

        return dispatchMessageSetting;
    }

    public JLabel makeHelper(String tooltip) {
        JLabel helpLable = new JLabel();
        helpLable.setIcon(new ImageIcon(getClass().getResource("/images/help.png")));
        helpLable.setToolTipText(tooltip);
        return helpLable;
    }
}
