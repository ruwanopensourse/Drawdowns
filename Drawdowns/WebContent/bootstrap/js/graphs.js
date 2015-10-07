function myFunction(info){
var chart = c3.generate({
    bindto: '#scatter_plot',
     size: {
        height: 500,
    },
    data: {
        xs: {
            High: 'High_x',
            HighMedium: 'HighMedium_x',
            Medium: 'Medium_x',
            MediumLow: 'MediumLow_x',
            Low: 'Low_x', 
        },
        url:info,
        mimeType: 'json',
        type: 'scatter',
        colors: {
            High: '#CC0000',
            HighMedium: '#FF0000',
            Medium: '#FF9999',
            MediumLow: '#3399FF',
            Low: '#0A1F33',
        },
    },
    axis: {
        x: {
            type: 'timeseries',
            label: 'Time',
            tick: {
                format: '%Y-%m-%d',
                rotate:90,
                fit: false
            }
        },
        y: {
            label: 'Permno',
        }
    },
    grid: {
        x: {
            show: true
        },
    },
     subchart: {
        show: true
    },
    
});
}
