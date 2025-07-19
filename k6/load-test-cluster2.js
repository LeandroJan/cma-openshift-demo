import http from "k6/http";
import { check } from "k6";

export let options = {
  scenarios: {
    constant_rate: {
      executor: "constant-arrival-rate",
      rate: 195, // 50 iterações (requests) por segundo
      timeUnit: "1s", // define a “janela” de 1 segundo
      duration: "5m", // duração total do teste
      preAllocatedVUs: 10, // VUs inicialmente alocados
      maxVUs: 1000, // máximo de VUs que podem ser escalados
    },
  },
  insecureSkipTLSVerify: true,
  thresholds: {
    http_req_duration: ["p(95)<1500"],
  },
};

export default function () {
  const res = http.get(
    "https://cma-openshift-demo-cma.apps.meu-cluster.sandbox2582.opentlc.com/api/products",
  );
  check(res, { "status was 200": (r) => r.status === 200 });
}
