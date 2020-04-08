package org.opencds.cqf.cql.retrieve;

import java.util.Iterator;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.cql.exception.UnknownElement;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;

public class FhirBundleCursor implements Iterable<Object> {

    public FhirBundleCursor(IGenericClient fhirClient, IBaseBundle results)
    {
        this(fhirClient, results, null);
    }

    // This constructor filters the bundle based on dataType
    public FhirBundleCursor(IGenericClient fhirClient, IBaseBundle results, String dataType) {
        this.fhirClient = fhirClient;
        this.results = results;
        this.dataType = dataType;
    }

    private IGenericClient fhirClient;
    private IBaseBundle results;
    private String dataType;


    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    public Iterator<Object> iterator() {
        return new FhirBundleIterator(fhirClient, results, dataType);
    }

    private class FhirBundleIterator implements Iterator<Object> {
        public FhirBundleIterator(IGenericClient fhirClient, IBaseBundle results, String dataType) {
            this.fhirClient = fhirClient;
            this.results =  results;
            this.current = -1;
            this.dataType = dataType;

            if (dataType != null) {
                this.dataTypeClass = this.fhirClient.getFhirContext().getResourceDefinition(this.dataType).getImplementingClass();
            }

            this.currentEntry = this.getEntry();
        }

        private IGenericClient fhirClient;
        private IBaseBundle results;
        private int current;
        private String dataType;
        private Class<? extends IBaseResource> dataTypeClass;
        private List<? extends IBaseResource> currentEntry;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        public boolean hasNext() {
            return current < this.currentEntry.size() - 1
                    || this.getLink() != null;
        }

        private List<? extends IBaseResource> getEntry() {
            if (this.dataTypeClass != null)
            {
                return BundleUtil.toListOfResourcesOfType(this.fhirClient.getFhirContext(), this.results, this.dataTypeClass);
            }
            else {
                return BundleUtil.toListOfResources(this.fhirClient.getFhirContext(), this.results);
            }
        }

        private String getLink() {
            return BundleUtil.getLinkUrlOfType(this.fhirClient.getFhirContext(), this.results, IBaseBundle.LINK_NEXT);
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws UnknownElement if the iteration has no more elements
         */
        public Object next() {
            current++;
            if (current < this.currentEntry.size()) {
                return this.currentEntry.get(current);
            } else {
                this.results = fhirClient.loadPage().next(results).execute();
                this.currentEntry = getEntry();
                current = 0;
                if (current < this.currentEntry.size()) {
                    return this.currentEntry.get(current);
                }
            }

            // TODO: It would be possible to get here if the next link was present, but the returned page had 0 entries...
            throw new UnknownElement("The iteration has no more elements.");
        }
    }
}
