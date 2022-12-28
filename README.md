# RepairTweaks

Tweaks certain repair-related behaviours of minecraft.
- Anvil item repair : by default, repairing an item in an anvil quickly increases its repair cost, preventing from repairing the item indefinitely. This plugin allows to make repair *not* increase the repair cost.
- Mending behaviour : allow to change how mending operates. A bit untested, there are currently two modes : **classical mending behaviour** (allows to define the durability per xp), and **repair-cost lowering** behaviour where applying the mending enchant actually only decrease the repair cost of the item.

## Commands

`/rtreload` : reload / regenerate the config file

## Default config

See [config.yml](lib/src/main/resources/config.yml) for default config. Everything is disabled by default