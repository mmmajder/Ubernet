export function secondsToDhms(seconds: number) {
  seconds = Number(seconds);
  const d = Math.floor(seconds / (3600 * 24));
  const h = Math.floor(seconds % (3600 * 24) / 3600);
  const m = Math.floor(seconds % 3600 / 60);
  const s = Math.floor(seconds % 60);

  const dDisplay = d > 0 ? d + (d == 1 ? " day, " : " days, ") : "";
  const hDisplay = h > 0 ? h + (h == 1 ? " hour, " : " hours, ") : "";
  const mDisplay = m > 0 ? m + (m == 1 ? " minute, " : " minutes, ") : "";
  const sDisplay = s > 0 ? s + (s == 1 ? " second" : " seconds") : "";
  return dDisplay + hDisplay + mDisplay + sDisplay;
}

export function dateTimeNowToString(): string{
  function pad2(n:number) { return n < 10 ? '0' + n : n }
  const date = new Date();
  return pad2(date.getDate()) + "." + pad2(date.getMonth() + 1) + "." + date.getFullYear().toString() + ". " + pad2(date.getHours()) + ":" + pad2(date.getMinutes());
}

export function formatTime(list: number[]): string {
  return addZero(list[2]) + '.' + addZero(list[1]) + '.' + list[0] + ' ' + addZero(list[3]) + ':' + addZero(list[4]);
}

function addZero(n: number): string {
  if (n == 0)
    return '00';
  if (n < 10)
    return '0' + n;
  return n.toString();
}

export function secondsToHm(d: number) {
  let h = Math.floor(d / 3600);
  let m = Math.floor(d % 3600 / 60);
  let s = Math.floor(d % 3600 % 60);

  let hDisplay = h > 0 ? h + (h == 1 ? " hour, " : " hours, ") : "";
  let mDisplay = m > 0 ? m + (m == 1 ? " minute, " : " minutes, ") : "";
  return hDisplay + mDisplay;
}
