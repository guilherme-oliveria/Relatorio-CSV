package br.jus.tjro.gabinete.infrastructure;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

@AnalyzeClasses(packages = ArchUnitTests.SEARCH_PACKAGE)
public class ArchUnitTests {
    public final static String SEARCH_PACKAGE = "br.jus.tjro";
    private JavaClasses testClasses;

    @BeforeEach
    public void setUp() {
        this.testClasses = new ClassFileImporter().importPackages(SEARCH_PACKAGE);
    }

    @Test
    public void noJUnit4() {
        var rule1 = noClasses()
            .should()
            .accessClassesThat()
            .resideInAnyPackage("org.junit");

        rule1.check(this.testClasses);

        var rule2 = noMethods()
            .should()
            .beAnnotatedWith(org.junit.Test.class)
            .because("we consistently want to use Jupiter in our tests");

        rule2.check(this.testClasses);
    }
}
