package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePanel extends JPanel {

    // todo: migrate to new Java time classes

    private final JLabel dayOfWeekLabel;
    private final JLabel dateLabel;

    public static DatePanel from(final Calendar calendar) {
        return new DatePanel(calendar);
    }

    private DatePanel(final Calendar calendar) {
        super();

        dayOfWeekLabel = new JLabel();
        dayOfWeekLabel.setFont(dayOfWeekLabel.getFont().deriveFont(Settings.HEADER_FONT_SIZE));

        dateLabel = new JLabel();
        dateLabel.setFont(dateLabel.getFont().deriveFont(Settings.FONT_SIZE));

        setDate(calendar);

        setLayout(new MigLayout("insets 2","[grow]","[grow][grow]"));
        setBackground(Settings.HEADER_BACKGROUND);
        add(dayOfWeekLabel, "wrap, ax center");
        add(dateLabel, "ax center, top push");
    }

    public void setDate(final Calendar calendar) {
        final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        final String weekDay = dayFormat.format(calendar.getTime());
        dayOfWeekLabel.setText(weekDay);

        dateLabel.setText(String.format(
            "%s/%s/%s",
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DATE),
            calendar.get(Calendar.YEAR)
        ));
    }

    public Calendar getDate() {
        final String[] dateTokens = dateLabel.getText().split("/");

        final Calendar calendar = Calendar.getInstance();
        calendar.set(
            Integer.parseInt(dateTokens[2]),
            Integer.parseInt(dateTokens[0]) - 1,
            Integer.parseInt(dateTokens[1])
        );

        return calendar;
    }
}
