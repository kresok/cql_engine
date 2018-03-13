package org.opencds.cqf.cql.runtime;

import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;

/**
 * Created by Bryn on 4/15/2016.
 */
public class Code {

    private String code;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Code withCode(String code) {
        setCode(code);
        return this;
    }

    private String display;
    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }
    public Code withDisplay(String display) {
        setDisplay(display);
        return this;
    }

    private String system;
    public String getSystem() {
        return system;
    }
    public void setSystem(String system) {
        this.system = system;
    }
    public Code withSystem(String system) {
        setSystem(system);
        return this;
    }

    private String version;
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public Code withVersion(String version) {
        setVersion(version);
        return this;
    }

    public Boolean equivalent(Code other) {
        return EquivalentEvaluator.equivalent(this.getCode(), other.getCode())
                && EquivalentEvaluator.equivalent(this.getSystem(), other.getSystem());
    }

    public Boolean equal(Code other) {
        Boolean codeIsEqual = EqualEvaluator.equal(this.getCode(), other.getCode());
        Boolean systemIsEqual = EqualEvaluator.equal(this.getSystem(), other.getSystem());
        Boolean versionIsEqual = EqualEvaluator.equal(this.getVersion(), other.getVersion());
        Boolean displayIsEqual = EqualEvaluator.equal(this.getDisplay(), other.getDisplay());
        return (codeIsEqual == null || systemIsEqual == null || versionIsEqual == null || displayIsEqual == null)
                ? null : codeIsEqual && systemIsEqual && versionIsEqual && displayIsEqual;
    }

    @Override
    public String toString() {
        return String.format(
                "Code { code: %s, system: %s, version: %s, display: %s }",
                getCode(), getSystem(), getVersion(), getDisplay()
        );
    }

}
