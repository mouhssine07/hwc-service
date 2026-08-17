Génère une mini-stratégie Marketing Digital personnalisée à partir de l'état validé.

N'invente aucune donnée. Indique clairement les hypothèses et les points restant à vérifier. Justifie chaque canal par l'objectif, la cible, l'existant, le budget et les ressources. Propose exactement quatre semaines d'actions réalistes et uniquement les KPI utiles.

Retourne uniquement un JSON valide respectant ces clés :

```json
{
  "sessionId": "",
  "companySummary": "",
  "currentAudit": "",
  "smartObjective": "",
  "targetAudience": {},
  "positioning": {},
  "priorityChannels": [],
  "fourWeekPlan": [],
  "budgetAndResources": {},
  "kpis": [],
  "assumptions": [],
  "pointsToVerify": [],
  "immediatePriority": ""
}
```

Le tableau `priorityChannels` contient au maximum trois canaux. Le tableau `fourWeekPlan` contient exactement quatre éléments.
