package com.github.cg;

import com.github.cg.generate.impl.CodeGenerateOne;
import com.github.cg.pojo.TableVo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lombok.extern.slf4j.Slf4j;
import com.github.cg.database.DbReadTableUtil;

/**
 * 应用入口
 *
 * @author: BenHsu
 * @version: 1.0
 * @date: 2019-05-10
 */
@Slf4j
public class CodeWindow extends JFrame {

    private static final long b = -5324160085184088010L;
    private static String c = "test";
    private static String d = "TestEntity";
    private static String e = "t00_company";
    private static String f = "分公司";
    private static Integer g = 1;
    private static String h = "uuid";
    private static String i = "";
    String[] idTypes = new String[]{"uuid", "identity", "sequence"};
    private static int LD_ROW_NUM = 12;
    private static int LD_COL_NUM = 2;
    private static int COL_LENGTH = 20;

    public CodeWindow() {
        JPanel panel = new JPanel();
        this.setContentPane(panel);
        panel.setLayout(new GridLayout(LD_ROW_NUM, LD_COL_NUM));
        JLabel tipLabel = new JLabel(" 提示:");
        final JLabel tipShow = new JLabel();
        JLabel pkgLabel = new JLabel(" 包名（小写）：");
        final JTextField pkgTxtField = new JTextField();
        JLabel entityLabel = new JLabel(" 实体类名（首字母大写）：");
        final JTextField entityValue = new JTextField();
        JLabel tableLabel = new JLabel(" 表名：");
        final JTextField tableValue = new JTextField(COL_LENGTH);
        JLabel idTypeLabel = new JLabel(" 主键生成策略：");
        final JComboBox idTypeCombo = new JComboBox(this.idTypes);
        idTypeCombo.setEnabled(false);
//        JLabel sequenceNameLabel = new JLabel("主键SEQUENCE：(oracle序列名)");
//        panel.add(sequenceNameLabel);
//        final JTextField sequenceNameValue = new JTextField(COL_LENGTH);
//        panel.add(sequenceNameValue);
        JLabel descrLabel = new JLabel(" 功能描述：");
        final JTextField descrValue = new JTextField();
        JLabel fieldNumLabel = new JLabel(" 行字段数目：");
        JTextField fieldNumValue = new JTextField();
        fieldNumValue.setText(g + "");
        ButtonGroup var18 = new ButtonGroup();
//        JRadioButton var19 = new JRadioButton("抽屉风格表单");
//        var19.setSelected(true);
//        JRadioButton var20 = new JRadioButton("弹窗风格表单");
//        var18.add(var19);
//        var18.add(var20);
        JCheckBox controlCB = new JCheckBox("Control");
        controlCB.setSelected(true);
        JCheckBox vueCB = new JCheckBox("Vue");
        vueCB.setSelected(false);
        JCheckBox serviceCB = new JCheckBox("Service");
        serviceCB.setSelected(true);
        JCheckBox mapperCB = new JCheckBox("Mapper.xml");
        mapperCB.setSelected(true);
        JCheckBox daoCB = new JCheckBox("Dao");
        daoCB.setSelected(true);
        JCheckBox entityCB = new JCheckBox("Entity");
        entityCB.setSelected(true);
        panel.add(tipLabel);
        panel.add(tipShow);
        panel.add(pkgLabel);
        panel.add(pkgTxtField);
        panel.add(entityLabel);
        panel.add(entityValue);
        panel.add(tableLabel);
        panel.add(tableValue);
        panel.add(idTypeLabel);
        panel.add(idTypeCombo);
//        panel.add(sequenceNameLabel);
//        panel.add(sequenceNameValue);
        panel.add(descrLabel);
        panel.add(descrValue);
        panel.add(fieldNumLabel);
        panel.add(fieldNumValue);
        panel.add(controlCB);
//        panel.add(vueCB);
        panel.add(new JLabel());
        panel.add(serviceCB);
        panel.add(mapperCB);
        panel.add(daoCB);
        panel.add(entityCB);
//        panel.add(var19);
//        panel.add(var20);
        JButton genBtn = new JButton("生成");
        genBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!"".equals(pkgTxtField.getText())) {
                    CodeWindow.c = pkgTxtField.getText();
                    if (!"".equals(entityValue.getText())) {
                        CodeWindow.d = entityValue.getText();
                        if (!"".equals(descrValue.getText())) {
                            CodeWindow.f = descrValue.getText();
                            if (!"".equals(tableValue.getText())) {
                                CodeWindow.e = tableValue.getText();
                                CodeWindow.h = (String) idTypeCombo.getSelectedItem();
//                                if (CodeWindow.h.equals("sequence")) {
//                                    if ("".equals(sequenceNameValue.getText())) {
//                                        tipShow.setForeground(Color.red);
//                                        tipShow.setText("主键生成策略为sequence时，序列号不能为空！");
//                                        return;
//                                    }
//                                    CodeWindow.i = sequenceNameValue.getText();
//                                }

                                try {
                                    boolean var2 = DbReadTableUtil.c(CodeWindow.e);
                                    if (var2) {
                                        TableVo var3x = new TableVo();
                                        var3x.setTableName(CodeWindow.e);
                                        var3x.setPrimaryKeyPolicy(CodeWindow.h);
                                        var3x.setEntityPackage(CodeWindow.c);
                                        var3x.setEntityName(CodeWindow.d);
                                        var3x.setFieldRowNum(CodeWindow.g);
                                        var3x.setSequenceCode(CodeWindow.i);
                                        var3x.setFtlDescription(CodeWindow.f);
                                        (new CodeGenerateOne(var3x)).generateCodeFile((String) null);
                                        tipShow.setForeground(Color.red);
                                        tipShow.setText("成功生成增删改查->功能：" + CodeWindow.f);
                                    } else {
                                        tipShow.setForeground(Color.red);
                                        tipShow.setText("表[" + CodeWindow.e + "] 在数据库中，不存在");
                                        log.error(" ERROR ：   表 [ " + CodeWindow.e + " ] 在数据库中，不存在 ！请确认数据源配置是否配置正确、表名是否填写正确~ ");
                                    }
                                } catch (Exception var4) {
                                    tipShow.setForeground(Color.red);
                                    tipShow.setText(var4.getMessage());
                                }

                            } else {
                                tipShow.setForeground(Color.red);
                                tipShow.setText("表名不能为空！");
                            }
                        } else {
                            tipShow.setForeground(Color.red);
                            tipShow.setText("描述不能为空！");
                        }
                    } else {
                        tipShow.setForeground(Color.red);
                        tipShow.setText("实体类名不能为空！");
                    }
                } else {
                    tipShow.setForeground(Color.red);
                    tipShow.setText("包名不能为空！");
                }
            }
        });
        JButton exitBtn = new JButton("退出");
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CodeWindow.this.dispose();
                System.exit(0);
            }
        });
        panel.add(genBtn);
        panel.add(exitBtn);
        this.setTitle("代码生成器[单表模型]");
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
        this.setSize(new Dimension(800, 600));
        this.setResizable(false);
        this.setLocationRelativeTo(this.getOwner());
    }

    public static void main(String[] args) {
        try {
            (new CodeWindow()).pack();
        } catch (Exception var2) {
            log.error(var2.getMessage());
        }

    }
}
