package com.kms.katalon.core.keyword.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Objects;

public class KeywordContributorCollection {

    private static List<IKeywordContributor> keywordContributors = new ArrayList<IKeywordContributor>();

    public static void addKeywordContributor(IKeywordContributor contributor) {
        keywordContributors.add(contributor);
    }

    public static List<IKeywordContributor> getKeywordContributors() {
        return keywordContributors;
    }

    public static IKeywordContributor getContributor(String keywordClassName) {
        Optional<IKeywordContributor> contributorOpt = keywordContributors.stream()
                .filter(contributor -> Objects.equal(contributor.getKeywordClass().getName(), keywordClassName))
                .findFirst();
        return contributorOpt.isPresent() ?  contributorOpt.get() : null;
    }
}
