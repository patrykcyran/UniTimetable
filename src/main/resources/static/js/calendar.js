$(function () {

    function displayinfo(event, elements) {
        var name = event.title;
        console.log(name);
        var appointmentStart = moment(event.start).format("YYYY-MM-DD HH:mm:ss")
        alert(name + "  " + appointmentStart)
    }

    $(document).ready(async function () {
        let url = '/appointment/';
        let patient_pesel = '[[${pesel}]]';
        if (patient_pesel !== "") {
            url = '/appointment/patient/';
        }else
            patient_pesel = 'all';
        url = url.concat(patient_pesel);
        console.log(patient_pesel);
        console.log(url);
        $('#calendar').fullCalendar({
            lang: 'pl',
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay'
            },
            defaultDate: moment().format("YYYY-MM-DD"),
            editable: false,
            eventLimit: true,
            events: {
                url: url,
                error: function () {
                    alert('there was an error while fetching events!');
                }
            },
            selectable: true,
            selectHelper: true,
            loading: function (bool) {
                $('#loading').toggle(bool);
            },
            eventClick: function (event, element) {
                displayinfo(event, element);
            },
        });
    });
});