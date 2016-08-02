package com.andrew.verhagen.line.gambit.systems.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.Component;
import com.artemis.World;

import java.util.HashMap;
import java.util.Map;

public class ArchetypeHolder {
    private World archetypeWorld;
    private Map<String, Archetype> archetypeMap;

    public ArchetypeHolder(World archetypeWorld) {
        this.archetypeWorld = archetypeWorld;
        this.archetypeMap = new HashMap<String, Archetype>();
        this.archetypeMap.put(null, null);
    }

    public Archetype getArchetype(String name) {
        return archetypeMap.get(name);
    }

    public void createArchetype(String name, Class<? extends Component>... components) {
        this.createArchetype(name, null, components);
    }

    public void createArchetype(String name, String parentName, Class<? extends Component>... components) {
        if (!archetypeMap.containsKey(name)) {
            Archetype parent = archetypeMap.get(parentName);
            Archetype archetype = new ArchetypeBuilder(parent).add(components).build(archetypeWorld);
            archetypeMap.put(name, archetype);
        } else {
            throw new RuntimeException("Archetype " + name + " already created.");
        }
    }
}
