package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.AcceptanceCriteria;

/**
 * Creates an Acceptance Criteria Element for the XML reports
 * Created by simon on 8/07/15.
 */
public class AcceptanceCriteriaElement
{
    public static AcceptanceCriteriaElement acceptanceCriteria;
    private Element parent = new Element("AcceptanceCriterion");

    /**
     * Constructor for the AC element
     */
    public AcceptanceCriteriaElement() {acceptanceCriteria = this; }

    /**
     * Returns the acceptance criteria element containing attributes:
     * Description
     * @param ac the acceptance criteria to generate the element for
     * @return the element with the acceptance criteria details attached
     */
    public Element getAcceptanceCriteria(AcceptanceCriteria ac) {
        parent.setAttribute("Description", ac.getText());
        return parent;
    }
}
