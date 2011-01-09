/*
 * PrintableContext.java
 *
 */
package net.sf.openrocket.gui.print;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Instances of this class are meant to keep track of what the user has selected to be printed.
 */
public class PrintableContext implements Comparable<PrintableContext>, Iterable<PrintableContext> {

    /**
     * The stage number.  May be null for printables that have no stage meaning.
     */
    private Set<Integer> stageNumber;

    /**
     * The type of thing to be printed.
     */
    private OpenRocketPrintable printable;

    private final Map<OpenRocketPrintable, Set<Integer>> previous = new TreeMap<OpenRocketPrintable, Set<Integer>>();

    
    public PrintableContext () {
    }

    /**
     * Constructor.
     *
     * @param theStageNumber the stage number of the printable; may be null if not applicable
     * @param thePrintable   the type of the thing to be printed
     *
     * @throws IllegalArgumentException thrown if thePrintable.isStageSpecific
     */
    private PrintableContext (final Set<Integer> theStageNumber, final OpenRocketPrintable thePrintable)
            throws IllegalArgumentException {
        if (thePrintable.isStageSpecific() && theStageNumber == null) {
            throw new IllegalArgumentException("A stage number must be provided when a printable is stage specific.");
        }
        stageNumber = theStageNumber;
        printable = thePrintable;
    }

    public void add (final Integer theStageNumber, final OpenRocketPrintable thePrintable)
            throws IllegalArgumentException {
        Set<Integer> stages = previous.get(thePrintable);
        if (stages == null) {
            stages = new TreeSet<Integer>();
            previous.put(thePrintable, stages);
        }
        if (theStageNumber != null) {
            stages.add(theStageNumber);
        }
    }


    public Iterator<PrintableContext> iterator () {
        return new Iterator<PrintableContext>() {

            Iterator<OpenRocketPrintable> keyIter = previous.keySet().iterator();

            @Override
            public boolean hasNext () {
                return keyIter.hasNext();
            }

            @Override
            public PrintableContext next () {
                final OpenRocketPrintable key = keyIter.next();
                return new PrintableContext(previous.get(key), key);
            }

            @Override
            public void remove () {
            }
        };

    }

    /**
     * Get the stage number, if it's applicable to the printable.
     *
     * @return the stage number
     */
    public Set<Integer> getStageNumber () {
        return stageNumber;
    }

    /**
     * Get the printable.
     *
     * @return the printable
     */
    public OpenRocketPrintable getPrintable () {
        return printable;
    }

    @Override
    public int compareTo (final PrintableContext other) {
        return this.printable.getPrintOrder() - other.printable.getPrintOrder();
    }

}