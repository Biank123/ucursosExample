import React, { useEffect, useState } from 'react';
import FullCalendar from '@fullcalendar/react'; // Importar FullCalendar
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';

// Importar Bootstrap CSS
import 'bootstrap/dist/css/bootstrap.min.css'; // Estilo de Bootstrap

interface Event {
  title: string;
  start: string;
  end?: string; // La propiedad end puede ser opcional
}

const Calendar: React.FC = () => {
  const [events, setEvents] = useState<Event[]>([]);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const response = await fetch('https://tu-api.com/api/events'); // URL de tu API
        const data = await response.json();
        setEvents(data);
      } catch (error) {
        console.error('Error al cargar eventos:', error);
      }
    };

    fetchEvents();
  }, []);

  return (
    <div>
      <h2>Calendario de Cursos</h2>
      <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        events={events}
        headerToolbar={{
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay',
        }}
        eventClick={(eventInfo) => alert(`Evento: ${eventInfo.event.title}`)}
      />
    </div>
  );
};

export default Calendar;