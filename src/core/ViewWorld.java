package core;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ViewWorld extends JFrame {
  
  private static final long serialVersionUID = 1L;
  AbstractWorld<?>          world;
  private JTextArea         text;
  
  public ViewWorld(final AbstractWorld<?> env) {
    super();
    
    this.world = env;
    
    init();
  }
  
  public void init() {
    final Dimension DimMax = Toolkit.getDefaultToolkit().getScreenSize();
    setMaximumSize(DimMax);
    
    this.text = new JTextArea("", 1 + 2 * this.world.getHeight(), 1 + 2 * this.world.getWidth());
    this.text.setEditable(false);
    this.text.setFont(new Font("Courier", Font.PLAIN, 12));
    final JScrollPane scrollPane = new JScrollPane(this.text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.getContentPane().add(scrollPane);
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setVisible(true);
  }
  
  public void refresh(final Integer currentDay) {
    final LocalDate now = LocalDate.of(2000, Month.JANUARY, 1);
    final Period period = Period.between(now, now.plusDays(currentDay));
    final String strToDisplay = "YEAR " + period.getYears() + " MONTH " + period.getMonths() + " DAY " + period.getDays() + "\n\n" + this.world.toString();
    this.text.setText(strToDisplay);
  }
}
