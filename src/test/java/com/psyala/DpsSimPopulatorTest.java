package com.psyala;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class DpsSimPopulatorTest extends TestCase {

    @Test
    public void testGetSimcGitRevision() {
        {
            String input = "SimulationCraft 915-01 for World of Warcraft 9.1.5.41359 Live (hotfix 2021-12-14/41359, git build shadowlands f0b29f0)";
            String expectedOutput = "f0b29f0";
            Assert.assertEquals(expectedOutput, DpsSimPopulator.getSimcGitRevision(input));
        }

        {
            String input = "SimulationCraft 915-01 for World of Warcraft 9.1.5.41359 Live (hotfix 2021-12-14/41359, git build f0b29f0)";
            String expectedOutput = "f0b29f0";
            Assert.assertEquals(expectedOutput, DpsSimPopulator.getSimcGitRevision(input));
        }
    }
}