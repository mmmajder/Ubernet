export function secondsToDhms(seconds: number) {
  seconds = Number(seconds);
  let d = Math.floor(seconds / (3600 * 24));
  let h = Math.floor(seconds % (3600 * 24) / 3600);
  let m = Math.floor(seconds % 3600 / 60);
  let s = Math.floor(seconds % 60);

  let dDisplay = d > 0 ? d + (d == 1 ? " day, " : " days, ") : "";
  let hDisplay = h > 0 ? h + (h == 1 ? " hour, " : " hours, ") : "";
  let mDisplay = m > 0 ? m + (m == 1 ? " minute, " : " minutes, ") : "";
  let sDisplay = s > 0 ? s + (s == 1 ? " second" : " seconds") : "";
  return dDisplay + hDisplay + mDisplay + sDisplay;
}

export function dateTimeNowToString(): string{
  function pad2(n:number) { return n < 10 ? '0' + n : n }
  let date = new Date();
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
