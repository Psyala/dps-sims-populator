package com.psyala.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class SimcOutputJSON {
    public String version;
    public String report_version;
    public int ptr_enabled;
    public int beta_enabled;
    public String build_date;
    public String build_time;
    public int timestamp;
    public String git_revision;
    public String git_branch;
    public Sim sim;

    public static class Rng {
        public String name;
    }

    public static class Live {
        public int build_level;
        public String wow_version;
        public String hotfix_date;
        public int hotfix_build;
        public String hotfix_hash;
    }

    public static class PTR {
        public int build_level;
        public String wow_version;
        public String hotfix_date;
        public int hotfix_build;
        public String hotfix_hash;
    }

    public static class Dbc {
        @JsonProperty("Live")
        public Live live;
        @JsonProperty("PTR")
        public PTR pTR;
        public String version_used;
    }

    public static class Options {
        public boolean debug;
        public double max_time;
        public double expected_iteration_time;
        public double vary_combat_length;
        public int iterations;
        public double target_error;
        public int threads;
        public String seed;
        public boolean single_actor_batch;
        public double queue_lag;
        public double queue_lag_stddev;
        public double gcd_lag;
        public double gcd_lag_stddev;
        public double channel_lag;
        public double channel_lag_stddev;
        public double queue_gcd_reduction;
        public boolean strict_gcd_queue;
        public double confidence;
        public double confidence_estimator;
        public double world_lag;
        public double world_lag_stddev;
        public double travel_variance;
        public double default_skill;
        public double reaction_time;
        public double regen_periodicity;
        public double ignite_sampling_delta;
        public boolean fixed_time;
        public int optimize_expressions;
        public int optimal_raid;
        public int log;
        public int debug_each;
        public int stat_cache;
        public int max_aoe_enemies;
        public boolean show_etmi;
        public double tmi_window_global;
        public double tmi_bin_size;
        public double enemy_death_pct;
        public boolean challenge_mode;
        public int timewalk;
        public boolean pvp_crit;
        public Rng rng;
        public int deterministic;
        public int average_range;
        public int average_gauss;
        public String fight_style;
        public double default_aura_delay;
        public double default_aura_delay_stddev;
        public Dbc dbc;
    }

    public static class Overrides {
        public int arcane_intellect;
        public int battle_shout;
        public int power_word_fortitude;
        public int chaos_brand;
        public int mystic_touch;
        public int mortal_wounds;
        public int bleeding;
        public int bloodlust;
    }

    public static class Talent {
        public int tier;
        public int id;
        public int spell_id;
        public String name;
    }

    public static class FightLength {
        public double sum;
        public int count;
        public double mean;
        public double min;
        public double max;
        public double median;
        public double variance;
        public double std_dev;
        public double mean_variance;
        public double mean_std_dev;
    }

    public static class ExecutedForegroundActions {
        public double sum;
        public int count;
        public double mean;
        public double min;
        public double max;
    }

    public static class Dmg {
        public double sum;
        public int count;
        public double mean;
        public double min;
        public double max;
    }

    public static class CompoundDmg {
        public double sum;
        public int count;
        public double mean;
        public double min;
        public double max;
    }

    public static class TimelineDmg {
        public double mean;
        public double mean_std_dev;
        public double min;
        public double max;
        public List<Double> data;
    }

    public static class Dps {
        public double sum;
        public int count;
        public double mean;
        public double min;
        public double max;
        public double median;
        public double variance;
        public double std_dev;
        public double mean_variance;
        public double mean_std_dev;
    }

    public static class Dpse {
        public double sum;
        public int count;
        public double mean;
        public double min;
        public double max;
    }

    public static class TargetMetric {
        public double sum;
        public int count;
        public double mean;
        public double min;
        public double max;
        public double median;
        public double variance;
        public double std_dev;
        public double mean_variance;
        public double mean_std_dev;
    }

    public static class Attribute {
        public double strength;
        public double agility;
        public double stamina;
        public double intellect;
    }

    public static class Resources {
        public double mana;
        public double soul_shard;
    }

    public static class Stats {
        public double spell_power;
        public double spell_crit;
        public double attack_crit;
        public double spell_haste;
        public double attack_haste;
        public double spell_speed;
        public double attack_speed;
        public double mastery_value;
        public double damage_versatility;
        public double heal_versatility;
        public double mitigation_versatility;
        public double crit_rating;
        public double crit_pct;
        public double haste_rating;
        public double haste_pct;
        public double mastery_rating;
        public double mastery_pct;
        public double versatility_rating;
        public double versatility_pct;
        public double speed_pct;
        public double manareg_per_second;
        public double armor;
        public double dodge;
    }

    public static class BuffedStats {
        public Attribute attribute;
        public Resources resources;
        public Stats stats;
    }

    public static class CollectedData {
        public FightLength fight_length;
        public ExecutedForegroundActions executed_foreground_actions;
        public Dmg dmg;
        public CompoundDmg compound_dmg;
        public TimelineDmg timeline_dmg;
        public int total_iterations;
        public Dps dps;
        public Dpse dpse;
        public TargetMetric target_metric;
        public BuffedStats buffed_stats;
    }

    public static class Head {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
        public int stamina;
        public int crit_rating;
        public int haste_rating;
    }

    public static class Neck {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int stamina;
        public int crit_rating;
        public int haste_rating;
    }

    public static class Shoulders {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
        public int stamina;
        public int haste_rating;
        public int versatility_rating;
    }

    public static class Chest {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
        public int stamina;
        public int haste_rating;
        public int mastery_rating;
    }

    public static class Waist {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
        public int stamina;
        public int crit_rating;
        public int versatility_rating;
    }

    public static class Legs {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
        public int stamina;
        public int haste_rating;
        public int mastery_rating;
    }

    public static class Feet {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
        public int stamina;
        public int haste_rating;
        public int mastery_rating;
    }

    public static class Wrists {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
        public int stamina;
        public int versatility_rating;
        public int mastery_rating;
    }

    public static class Hands {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
        public int stamina;
        public int haste_rating;
        public int mastery_rating;
    }

    public static class Finger1 {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int stamina;
        public int crit_rating;
        public int versatility_rating;
    }

    public static class Finger2 {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int stamina;
        public int haste_rating;
        public int mastery_rating;
    }

    public static class Trinket1 {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
    }

    public static class Trinket2 {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
    }

    public static class Back {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int stamina;
        public int crit_rating;
        public int mastery_rating;
        public int stragiint;
    }

    public static class MainHand {
        public String name;
        public String encoded_item;
        public int ilevel;
        public int intellect;
        public int stamina;
        public int crit_rating;
        public int mastery_rating;
    }

    public static class Gear {
        public Head head;
        public Neck neck;
        public Shoulders shoulders;
        public Chest chest;
        public Waist waist;
        public Legs legs;
        public Feet feet;
        public Wrists wrists;
        public Hands hands;
        public Finger1 finger1;
        public Finger2 finger2;
        public Trinket1 trinket1;
        public Trinket2 trinket2;
        public Back back;
        public MainHand main_hand;
    }

    public static class Custom {
    }

    public static class Player {
        public String name;
        public String race;
        public int level;
        public String role;
        public String specialization;
        public String profile_source;
        public List<Talent> talents;
        public int party;
        public int ready_type;
        public boolean bugs;
        public boolean scale_player;
        public boolean potion_used;
        public String timeofday;
        public String zandalari_loa;
        public String vulpera_tricks;
        public int invert_scaling;
        public double reaction_offset;
        public double reaction_max;
        public double reaction_mean;
        public double reaction_stddev;
        public double reaction_nu;
        public double world_lag;
        public double brain_lag;
        public double brain_lag_stddev;
        public boolean world_lag_override;
        public boolean world_lag_stddev_override;
        public Dbc dbc;
        public double base_mana_regen_per_second;
        public CollectedData collected_data;
        public Gear gear;
        public Custom custom;
    }

    public static class Result {
        public String name;
        public double mean;
        public double min;
        public double max;
        public double stddev;
        public double mean_stddev;
        public double mean_error;
        public double median;
        public double first_quartile;
        public double third_quartile;
        public int iterations;
    }

    public static class Profilesets {
        public String metric;
        public List<Result> results;
    }

    public static class SimulationLength {
        public double sum;
        public int count;
        public double mean;
        public double min;
        public double max;
        public double median;
        public double variance;
        public double std_dev;
        public double mean_variance;
        public double mean_std_dev;
    }

    public static class RaidDps {
        public double sum;
        public int count;
        public double mean;
    }

    public static class TotalDmg {
        public double sum;
        public int count;
        public double mean;
    }

    public static class Statistics {
        public double elapsed_cpu_seconds;
        public double elapsed_time_seconds;
        public double init_time_seconds;
        public double merge_time_seconds;
        public double analyze_time_seconds;
        public SimulationLength simulation_length;
        public int total_events_processed;
        public RaidDps raid_dps;
        public TotalDmg total_dmg;
    }

    public static class Sim {
        public Options options;
        public Overrides overrides;
        public List<Player> players;
        public Profilesets profilesets;
        public Statistics statistics;
    }
}



