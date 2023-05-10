package com.fifty.data.repository.skill

import com.fifty.data.models.Skill

interface SkillRepository {

    suspend fun getSkills(): List<Skill>
}