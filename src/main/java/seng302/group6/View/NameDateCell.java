package seng302.group6.View;

import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Custom List Cell for showing a name on the left with date range on the right
 * Created by simon on 15/05/15.
 */
public class NameDateCell extends HBox
{
    Label name = new Label();
    Label date = new Label();

    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a new NameDateCell with nameText on the left and formatted start
     * date and end date on the right
     * @param nameText left label
     * @param startDate first date of right label
     * @param endDate second date of right label
     */
    public NameDateCell(String nameText, LocalDate startDate, LocalDate endDate)
    {
        super();

        this.startDate = startDate;
        this.endDate = endDate;

        // Set up left name label
        name.setText(nameText);
        name.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(name, Priority.ALWAYS);
        name.getStyleClass().remove("label");

        // Set up dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        String dateString = dateFormat.format(start) + " - " +
                dateFormat.format(end);
        date.setText(dateString);

        // Change style of date range
        date.setStyle("-fx-text-fill: #555555;" +
                      "-fx-font-size: 9pt;");

        this.getChildren().addAll(name, date);
    }

    /**
     * Gets the text of the name label (the same value that is set in the
     * constructor)
     * @return name label text
     */
    public String getName()
    {
        return name.getText();
    }

    /**
     * Get start date
     * @return start date
     */
    public LocalDate getStartDate()
    {
        return startDate;
    }

    /**
     * Get end date
     * @return end date
     */
    public LocalDate getEndDate()
    {
        return endDate;
    }
}
