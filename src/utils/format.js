export function formatMoney(value) {
  const amount = Number(value || 0);
  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 2,
  }).format(amount);
}

export function formatDate(value) {
  if (!value) return "—";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return new Intl.DateTimeFormat("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  }).format(date);
}

export function formatTime(value) {
  if (!value) return "—";
  const parts = value.split(":");
  const hour = Number(parts[0]);
  const minute = parts[1] || "00";
  const suffix = hour >= 12 ? "PM" : "AM";
  const displayHour = hour % 12 || 12;
  return `${displayHour}:${minute} ${suffix}`;
}

export function formatDuration(minutes) {
  const duration = Number(minutes);
  if (!Number.isFinite(duration) || duration <= 0) return "Duration —";
  const hours = Math.floor(duration / 60);
  const remainingMinutes = duration % 60;
  if (!hours) return `${remainingMinutes} min`;
  if (!remainingMinutes) return `${hours} hr`;
  return `${hours} hr ${remainingMinutes} min`;
}

export function posterFallback(index = 0) {
  const posters = [
    "/src/assets/movies/movies1.png",
    "/src/assets/movies/movies2.webp",
    "/src/assets/movies/movies3.jpg",
    "/src/assets/movies/movies4.webp",
  ];
  return posters[index % posters.length];
}
