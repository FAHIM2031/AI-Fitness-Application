import React from 'react'
import Box from '@mui/material/Box';
import { Button } from "@mui/material"
import {
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField
} from "@mui/material";

import { useState } from 'react';

const ActivityForm = ({ onActivityAdded }) => {

    const [activity,setactivity]=useState({
        type: "RUNNING",
        duration: '',
        caloriesBurned: '',
        additionalMetrics: {}
    });


    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Simulate an API call to add the activity
            await addActivity(activity);
            onActivitiesAdded();
            setactivity({
                type: "RUNNING",
                duration: '',
                caloriesBurned: '',
                additionalMetrics: {}
            });
        } catch (error) {
            console.error('Error adding activity:', error);
        }
    };

    return (
     <Box component="form" sx={{ mb: 2 }} onSubmit={handleSubmit}>
<FormControl fullWidth sx={{ mb: 2 }}>
  <InputLabel >Activity Type</InputLabel>
  <Select
    
    value={activity.type}
    onChange={(e) => {setactivity({...activity,type: e.target.value})}}
  >
    <MenuItem value="RUNNING">Running</MenuItem>
    <MenuItem value="WALKING">Walking</MenuItem>
    <MenuItem value="CYCLING">Cycling</MenuItem>
  </Select>
            </FormControl>
    
            <TextField fullWidth label="Duration (minutes)" type="number" sx={{ mb: 2 }} value={activity.duration} onChange={(e) => { setactivity({ ...activity, duration: e.target.value }) }} />
            <TextField fullWidth label="Calories Burned" type="number" sx={{ mb: 2 }} value={activity.caloriesBurned} onChange={(e) => { setactivity({ ...activity, caloriesBurned: e.target.value }) }} />
            

            <Button type="submit" variant="contained" color="primary">
                ADD ACTIVITY


            </Button>
            

    </Box>
    )
}

export default ActivityForm;