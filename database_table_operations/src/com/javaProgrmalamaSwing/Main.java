package com.javaProgrmalamaSwing;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        DefaultTableModel modelMusteri = new DefaultTableModel();

        modelMusteri.addColumn("ID");
        modelMusteri.addColumn("FirstName");
        modelMusteri.addColumn("LastName");
        modelMusteri.addColumn("Phone");

        Connection baglanti;
        Statement st;

        try{
            Class.forName("org.mariadb.jdbc.Driver");
            baglanti = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classicmodels", "root","");
            System.out.println("Bağlantı başarılı");
            st = baglanti.createStatement();

            ResultSet sonuc = st.executeQuery("Select * from customers");
            while(sonuc.next())
            {
                modelMusteri.addRow(new Object[]{sonuc.getInt(1), sonuc.getString(4), sonuc.getString(3),sonuc.getString(5)});
            }

            JFrame cerceve = new JFrame("Table customers ");
            JButton buton =new JButton("değişiklikleri kaydet");
            JTable tablo = new JTable(modelMusteri);

            JScrollPane kaydirmaCubugu = new JScrollPane(tablo);
            kaydirmaCubugu.setBounds(10, 20, 775, 620);
            cerceve.add(kaydirmaCubugu);

            modelMusteri.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {


                    int n = JOptionPane.showConfirmDialog(
                            cerceve,
                            "değişiklikler daydedilsin mi",
                            "uyarı mesajı",
                            JOptionPane.YES_NO_OPTION);
                    if (n==0)
                    {
                        // System.out.println(tablo.getValueAt(tablo.getSelectedRow(),tablo.getSelectedColumn()));
                        // System.out.println(tablo.getValueAt(tablo.getSelectedRow(),0));
                        String degisen = String.valueOf(tablo.getValueAt(tablo.getSelectedRow(),tablo.getSelectedColumn()));
                        String degisenID = String.valueOf(tablo.getValueAt(tablo.getSelectedRow(),0));
                        String sorgu;
                        if (tablo.getSelectedColumn() == 1) {
                            sorgu = "UPDATE `customers` SET `contactFirstName` = '" +
                                    degisen +
                                    "' WHERE `customers`.`customerNumber` = " +
                                    degisenID +
                                    ";";
                        }
                        else if (tablo.getSelectedColumn() == 2) {
                            sorgu = "UPDATE `customers` SET `contactLastName` = '" +
                                    degisen +
                                    "' WHERE `customers`.`customerNumber` = " +
                                    degisenID +
                                    ";";
                        }
                        else if (tablo.getSelectedColumn() == 3) {
                            sorgu = "UPDATE `customers` SET `phone` = '" +
                                    degisen +
                                    "' WHERE `customers`.`customerNumber` = " +
                                    degisenID +
                                    ";";

                        }



                        else {
                            JOptionPane.showMessageDialog(cerceve, "ID değişikliği yapamazsınız...","Hata",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        try {
                            ResultSet sonuc = st.executeQuery(sorgu);
                            System.out.println(sonuc);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        System.out.println("değişiklik kaydedildi");

                    }
                    else if (n==1)
                    {
                        JOptionPane.showMessageDialog(cerceve,
                                "Değişiklik kaydedilmedi.",
                                "Uyarı Mesajı",
                                JOptionPane.WARNING_MESSAGE);

                    }

                }
            });

            cerceve.setBounds(250,10,800,700);
            cerceve.setLayout(null);
            cerceve.setVisible(true);
            cerceve.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}
