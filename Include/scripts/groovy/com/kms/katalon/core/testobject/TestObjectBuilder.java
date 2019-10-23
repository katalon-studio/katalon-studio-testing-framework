package com.kms.katalon.core.testobject;

import java.util.List;

public class TestObjectBuilder {
    private TestObject testObject;

    public TestObjectBuilder(String objectId) {
        testObject = new TestObject(objectId);
    }

    public TestObjectBuilder withParentObject(TestObject parentObject) {
        testObject.setParentObject(parentObject);
        return this;
    }

    public TestObjectBuilder withIsParentObjectShadowRootEqual(boolean val) {
        testObject.setParentObjectShadowRoot(val);
        return this;
    }

    public TestObjectBuilder withProperties(List<TestObjectProperty> properties) {
        testObject.setProperties(properties);
        return this;
    }

    public TestObjectBuilder withXPaths(List<TestObjectXpath> xpaths) {
        testObject.setXpaths(xpaths);
        return this;
    }

    public TestObjectBuilder withImagePath(String path) {
        testObject.setImagePath(path);
        return this;
    }

    public TestObjectBuilder withUseRelativeImagePathEqual(boolean val) {
        testObject.setUseRelativeImagePath(val);
        return this;
    }

    public TestObjectBuilder withSelectorMethod(SelectorMethod selectorMethod) {
        testObject.setSelectorMethod(selectorMethod);
        return this;
    }

    public TestObjectBuilder setSelectorValue(SelectorMethod selectorMethod, String selectorValue) {
        testObject.setSelectorValue(selectorMethod, selectorValue);
        return this;
    }

    public TestObject build() {
        return testObject;
    }

}
