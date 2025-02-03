package com.dreamgames.backendengineeringcasestudy.entity;

import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_progress")
public class UserProgress {

    @Id
    @Column("id")
    private Integer id;
    @Column("coin")
    private Double coin;
    @Column("level_at")
    private Integer levelAt;
    @Column("helium")
    private Double helium;
    @Column("test_group")
    private TestGroup testGroup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getCoin() {
        return coin;
    }

    public void setCoin(Double coin) {
        this.coin = coin;
    }

    public Integer getLevelAt() {
        return levelAt;
    }

    public void setLevelAt(Integer levelAt) {
        this.levelAt = levelAt;
    }

    public Double getHelium() {
        return helium;
    }

    public void setHelium(Double helium) {
        this.helium = helium;
    }

    public TestGroup getTestGroup() {
        return testGroup;
    }

    public void setTestGroup(TestGroup testGroup) {
        this.testGroup = testGroup;
    }
}
