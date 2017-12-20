/*
 * BenthicJuvStageAttributesCustomizer.java
 *
 * Created on January 12, 2006, 4:20 PM
 */

package sh.pcod.BenthicJuvStage;

import wts.models.DisMELS.framework.*;
import java.beans.Customizer;
import javax.swing.border.TitledBorder;
import wts.models.DisMELS.gui.AttributesCustomizer;

/**
 * @author William Stockhausen
 */
public class BenthicJuvStageAttributesCustomizer extends AttributesCustomizer {

    private boolean showHorizPos = true;
    private boolean showVertPos = true;
    
    private BenthicJuvStageAttributes attributes = null;
    
    /**
     * Creates new customizer BenthicJuvStageAttributesCustomizer
     */
    public BenthicJuvStageAttributesCustomizer() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        czrStandardAttributes = new wts.models.DisMELS.gui.AbstractLHSAttributesCustomizer();
        jPanel2 = new javax.swing.JPanel();
        jtfDevStage = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jtfDiameter = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtfDensity = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(czrStandardAttributes, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Additional attributes"));

        jtfDevStage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDevStage.setText("0");
        jtfDevStage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDevStageActionPerformed(evt);
            }
        });

        jLabel1.setText("Egg developement stage");

        jtfDiameter.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiameter.setText("0");
        jtfDiameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDiameterActionPerformed(evt);
            }
        });

        jLabel2.setText("Egg diameter (mm)");
        jLabel2.setToolTipText("diameter in mm");

        jtfDensity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDensity.setText("0");
        jtfDensity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDensityActionPerformed(evt);
            }
        });

        jLabel3.setText("Egg density (kg/m^3)");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jtfDevStage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
            .add(jPanel2Layout.createSequentialGroup()
                .add(jtfDiameter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
            .add(jPanel2Layout.createSequentialGroup()
                .add(jtfDensity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfDevStage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfDiameter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfDensity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3)))
        );

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jtfDevStageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfDevStageActionPerformed
        Double n = new Double(jtfDevStage.getText());
        attributes.setValue(attributes.PROP_devStage,n);
    }//GEN-LAST:event_jtfDevStageActionPerformed

    private void jtfDiameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfDiameterActionPerformed
        Double n = new Double(jtfDiameter.getText());
        attributes.setValue(attributes.PROP_length,n);
    }//GEN-LAST:event_jtfDiameterActionPerformed

    private void jtfDensityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfDensityActionPerformed
        Double n = new Double(jtfDensity.getText());
        attributes.setValue(attributes.PROP_density,n);
    }//GEN-LAST:event_jtfDensityActionPerformed

    @Override
    public void setObject(Object bean) {
        if (bean instanceof BenthicJuvStageAttributes) {
            setAttributes((BenthicJuvStageAttributes) bean);
        }
    }
    
    @Override
    public BenthicJuvStageAttributes getAttributes() {
        return attributes;
    }
    
    @Override
    public void setAttributes(LifeStageAttributesInterface newAtts) {
        if (newAtts instanceof BenthicJuvStageAttributes) {
            attributes = (BenthicJuvStageAttributes) newAtts;
            czrStandardAttributes.setObject(attributes);
            Double d = null;
            jtfDevStage.setText(attributes.getValue(attributes.PROP_devStage,d).toString());
            jtfDiameter.setText(attributes.getValue(attributes.PROP_length,d).toString());
            jtfDensity.setText(attributes.getValue(attributes.PROP_density,d).toString());
        }
    }
    
    @Override
    public void showHorizPos(boolean b) {
        czrStandardAttributes.showHorizPos(b);
        revalidate();
    }
    
    @Override
    public void showVertPos(boolean b) {
        czrStandardAttributes.showVertPos(b);
        revalidate();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private wts.models.DisMELS.gui.AbstractLHSAttributesCustomizer czrStandardAttributes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jtfDensity;
    private javax.swing.JTextField jtfDevStage;
    private javax.swing.JTextField jtfDiameter;
    // End of variables declaration//GEN-END:variables
    
}
